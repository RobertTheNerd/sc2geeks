<?php

include_once( dirname( __FILE__ ) . '/sc2lib.php' );


class WP_JSON_SC2Objects extends WP_JSON_Posts{
	/**
	 * Server object
	 *
	 * @var WP_JSON_ResponseHandler
	 */
	protected $server;

	/**
	 * Constructor
	 *
	 * @param WP_JSON_ResponseHandler $server Server object
	 */
	public function __construct(WP_JSON_ResponseHandler $server) {
		parent::__construct($server);
	}

	/**
	 * Register the post-related routes
	 * Response for all requests creating objects:
	 *      If an object is created, response code: 201
	 *      If an object is updated, response code: 200
	 *      Etherwise, 500.
	 *
	 * @param array $routes Existing routes
	 * @return array Modified routes
	 */
	public function register_routes( $routes ) {
		$post_routes = array(

			// endpoint to get all images for a given post
			'/sc2obj/img.*' => array(
				array( array( $this, 'get_sc2_images' ),       WP_JSON_Server::READABLE ),
			),

            // endpoint to get all sc2 objects
            '/sc2obj/(?P<id>\d+)' => array(
                array( array( $this, 'get_sc2_obj' ),       WP_JSON_Server::READABLE ),
            ),

            // endpoint to get all sc2 objects
            '/post/(?P<id>\d+)' => array(
                array( array( $this, 'get_post' ),       WP_JSON_Server::READABLE ),
            ),

            // endpoint to get all sc2 objects
            '/post/category/(?P<category>[\w\s]+)' => array(
                array( array( $this, 'get_tech_showoffs' ),       WP_JSON_Server::READABLE ),
            ),

            // endpoint to create/update map
			'/map' => array(
				array( array( $this, 'create_map' ),    WP_JSON_Server::CREATABLE | WP_JSON_Server::ACCEPT_JSON ),
			),

            // endpoint to create/update player
            '/player' => array(
                array( array( $this, 'create_player' ),    WP_JSON_Server::CREATABLE | WP_JSON_Server::ACCEPT_JSON ),
            ),

            // endpoint to create/update player
            '/progamer' => array(
                array( array( $this, 'create_progamer' ),    WP_JSON_Server::CREATABLE | WP_JSON_Server::ACCEPT_JSON ),
            ),

            // endpoint to create/update upload_task
            '/upload_task' => array(
                array( array( $this, 'update_upload_task' ),    WP_JSON_Server::CREATABLE | WP_JSON_Server::ACCEPT_JSON ),
            ),

            // endpoint to create/update upload_task
            '/auth' => array(
                array( array( $this, 'auth' ),    WP_JSON_Server::READABLE | WP_JSON_Server::ACCEPT_JSON ),
            ),

		);
		return array_merge( $routes, $post_routes );
	}

	public function get_sc2_images() {
		$ids = json_decode(get_query_var('ids'));
		$res = array();
		foreach ($ids as $id) {
			$image = get_post_image($id);
			if ($image == false)
				continue;

			WP_JSON_SC2Objects::remove_array_element($image, 'image_meta');
			if (array_key_exists('sizes', $image)) {
				WP_JSON_SC2Objects::remove_array_element($image['sizes'], 'thumbnail');
				WP_JSON_SC2Objects::remove_array_element($image['sizes'], 'medium');
			}
			$res[$id] = $image;
		}
		return $this->get_json_response($res);
	}

    public function get_post($id) {
        return $this->get_json_response(sc2_get_posts_by_id($id));
    }

    public function get_tech_showoffs($category){
        return $this->get_json_response(sc2_get_posts_by_category($category));
    }

	public function get_sc2_obj( $id ) {
		return $this->get_json_response(sc2_get_obj($id));
	}

	public function create_map($data) {
		return $this->do_create($data, 'sc2_create_map');
	}

    public function create_player($data) {
        _log('Create Player request accepted.');
        _log($data);
        return $this->do_create($data, 'sc2_create_player');
    }

    public function create_progamer($data) {
        return $this->do_create($data, 'sc2_create_progamer');
    }

	public function update_upload_task($data) {
		return $this->do_create($data, 'sc2_update_upload_task');
	}

	public function do_create($data, $do_function) {
		$result = call_user_func($do_function, $data);
		$response = json_ensure_response( $this->get_json_response(array('id' => $result['id'])) );
		if ($result['action'] == 'update')
			$response->set_status( 200 );
		else
			$response->set_status( 201 );

        if (array_key_exists('data', $result)) {
            $response->set_data($result['data']);
        }

		return $response;
	}

	public function get_json_response($data) {
		// Link headers (see RFC 5988)

		$response = new WP_JSON_Response();
		// $response->header( 'Last-Modified', mysql2date( 'D, d M Y H:i:s', $data['post_modified_gmt'] ) . 'GMT' );

		$response->set_data( $data );
		return $response;
	}

    public function auth() {
        $authorized_roles = ['Administrator', 'Replay Admin'];
        $response = json_ensure_response( $this->get_json_response(array()) );

        if ($this->auth_roles($authorized_roles)) {
            $response->set_status(200);
        } else {
            $response->set_status(403);
        }
        return $response;
    }

    private function auth_roles($authorized_roles) {
        global $current_user;
        $current_user = wp_get_current_user();

        // get option
        if (!($current_user instanceof WP_User)) {
            return false;
        }

        $all_roles = get_option('sc2_user_roles');

        foreach ($current_user->roles as $role) {
            if (array_key_exists($role, $all_roles)) {
                $role_name = $all_roles[$role]['name'];
                if (in_array($role_name, $authorized_roles))
                    return true;
            }
        }

        return false;
    }

	private static function remove_array_element($array, $index) {
		if (array_key_exists($index, $array))
			unset($array[$index]);
	}

}
