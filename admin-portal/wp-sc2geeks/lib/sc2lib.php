<?php
/**
 * Created by PhpStorm.
 * User: robert
 * Date: 8/5/14
 * Time: 3:17 PM
 */

include_once( dirname( __FILE__ ) . '/Unirest.php' );
include_once( dirname( __FILE__ ) . '/config.php' );

require_once(ABSPATH . 'wp-admin/includes/media.php');
require_once(ABSPATH . 'wp-admin/includes/file.php');
require_once(ABSPATH . 'wp-admin/includes/image.php');

/**
 * @param $hash
 * @returns map id
 */
function sc2_get_map_by_hash($hash) {
	global $wpdb;
	$sql = $wpdb->prepare("
		SELECT p.ID
		FROM $wpdb->postmeta meta
		INNER JOIN $wpdb->posts p
		ON meta.post_id=p.ID
		AND p.post_status in ('publish', 'draft')
		AND meta.meta_key='hash'
		AND meta.meta_value=%s
	", $hash);
	// $wpdb->show_errors();
	$result = $wpdb->get_row($sql, ARRAY_N);

	if (empty($result)) {
		return false;
	}
	return sc2_get_obj($result[0]);
}

/*
function sc2_get_map($post) {
	if (!empty($post)) {
		$id = $post['ID'];
		$map = array(
			'id'            => $id,
			'name'          => $post['post_title'],
			'hash'          => sc2_get_field('hash', $id),
			'url'           => sc2_get_field('url', $id),
			'server'        => sc2_get_field('server', $id),
			'image'         => get_post_image($id),
		);

		return $map;
	}
	return false;
}
*/

function sc2_create_map($data) {
    if (!array_key_exists('hash', $data))
        return;

	$hash = $data['hash'];
	$old_map = sc2_get_map_by_hash($hash);

	// update old map if needed
	if ($old_map) {
        $id = $old_map['id'];
        /*
         * Once a map exists, do not modify it's name as it'll be overwritten to another language.
         *
		if($old_map['server'] != $data['server']
			|| $old_map['url'] != $data['url']
			|| $old_map['name'] != $data['name']) {
			update_field('server', $data['server'], $id);
			update_field('url', $data['url'], $id);
			wp_update_post(array(
				'ID'            => $id,
				'post_title'    => $data['name'],
				'post_status'   => 'draft',
			));
		}
        */
		return array(
			'id'        => $id,
			'action'    => 'update',
		);
	}

	// insert new map
	$post = array(
		'post_title'            => $data['name'],
		'post_type'             => 'map',
		'post_status'           => 'draft',
	);

    remove_action('save_post', 'sc2_save_obj_to_mongo');
    $id = wp_insert_post($post);
	if ($id != 0) {
		update_field('hash', $data['hash'], $id);
		update_field('server', $data['server'], $id);
		update_field('url', $data['url'], $id);
	}
    sc2_save_obj_to_mongo($id);
    add_action('save_post', 'sc2_save_obj_to_mongo');

    return array(
		'id'        => $id,
		'action'    => 'create',
	);
}

function sc2_update_upload_task($data) {
	if (!array_key_exists('taskId', $data)) {
		return array(
			'id'        => 0,
			'action'    => 'Not exists',
		);
	}

	$id = $data['taskId'];

	// status
	if (array_key_exists('status', $data)) {
		wp_update_post(array(
			'ID'            => $id,
			'post_status'   => $data['status']
		));
	}

	$fields = ['total_progress', 'current_file', 'current_action', 'current_progress', 'message'];

	foreach ($fields as $field) {
		if (array_key_exists($field, $data)) {
			update_post_meta($id, $field, $data[$field]);
		}
	}
	return array(
		'id'        => $id,
		'action'    => 'Updated',
	);
}

function sc2_get_player_by_url($url) {
    global $wpdb;
    $sql = $wpdb->prepare("
		SELECT p.ID
		FROM $wpdb->postmeta meta
		INNER JOIN $wpdb->posts p
		ON meta.post_id=p.ID AND p.post_type = 'player'
		AND p.post_status in ('publish', 'draft')
		AND meta.meta_key='url'
		AND meta.meta_value=%s
	", $url);
    // $wpdb->show_errors();
    $result = $wpdb->get_row($sql, ARRAY_N);

    if (empty($result)) {
        return false;
    }
    return sc2_get_obj($result[0]);
}

function sc2_get_progamer_by_url($url) {
    global $wpdb;
    $sql = $wpdb->prepare("
		SELECT p.ID, p.post_status
		FROM $wpdb->postmeta meta
		INNER JOIN $wpdb->posts p
		ON meta.post_id=p.ID AND p.post_type = 'progamer'
		AND p.post_status in ('publish', 'draft')
		AND meta.meta_key='link'
		AND meta.meta_value=%s
	", $url);
    // $wpdb->show_errors();
    $result = $wpdb->get_row($sql, ARRAY_N);

    if (empty($result)) {
        return false;
    }
    return sc2_get_obj($result[0]);
}
function sc2_create_player($data) {
	$url = $data['url'] = strtolower($data['url']);
	$old_player = sc2_get_player_by_url($url);

	// a player will only change his/her name & race.
	// And user status will not change to draft.
	if ($old_player) {
		$id = $old_player['id'];
		if($old_player['name'] != $data['name']
			|| $old_player['play_race'] != $data['play_race']
			|| $old_player['pick_race'] != $data['pick_race']) {
			update_field('play_race', $data['play_race'], $id);
			update_field('pick_race', $data['pick_race'], $id);
            _log("Update existing player");
			wp_update_post(array(
				'ID'            => $id,
				'post_title'    => $data['name'],
			));
		}
		return array(
			'id'        => $id,
			'action'    => 'update',
		);
	}

	$post = array(
		'post_title'            => $data['name'],
		'post_type'             => 'player',
		'post_status'           => 'draft',
	);

    // create new player
    _log("create new player");
    remove_action('save_post', 'sc2_save_obj_to_mongo');
	$id = wp_insert_post($post);
	if ($id != 0) {
		update_field('url', $data['url'], $id);
		update_field('toon_id', $data['toon_id'], $id);
		update_field('toon_handle', $data['toon_handle'], $id);
		update_field('region', $data['region'], $id);
		update_field('subregion', $data['subregion'], $id);
		update_field('pick_race', $data['pick_race'], $id);
		update_field('play_race', $data['play_race'], $id);
	}
    sc2_save_obj_to_mongo($id);
    add_action('save_post', 'sc2_save_obj_to_mongo');
    return array(
		'id'        => $id,
		'action'    => 'create',
	);
}

function sc2_create_progamer($data) {
    $url = $data['link'];
    $old_progamer = sc2_get_progamer_by_url($url);
    $img = $data['large_image'];
    $msg = 'Done';

    if ($old_progamer) {
        _log("Old progamer found.");
        _log($old_progamer);
        $id = $old_progamer['id'];
        $status = $old_progamer['status'];

        // will only change progamer if status is 'draft'
        if ($status != 'draft') {
            return array(
                'id'        => $id,
                'action'    => 'NotAllowed',
                'data'      => array ('msg' => 'Progamer already exists and published. Will not update.')
            );
        }

        // update name only
        _log("Updating old progamer");
        wp_update_post(array(
            'ID'            => $id,
            'post_title'    => $data['name'],
        ));

        $action = 'update';
    } else {
        // insert new progamer
        remove_action('save_post', 'sc2_save_obj_to_mongo');
        $post = array(
            'post_title'            => $data['name'],
            'post_type'             => 'progamer',
            'post_status'           => 'draft',
        );
        $id = wp_insert_post($post);
        $action = 'create';
    }

    if ($id != 0) {
        update_field('native_name', $data['native_name'], $id);
        update_field('romanized_name', $data['romanized_name'], $id);
        update_field('birthday', $data['birthday'], $id);
        update_field('country', $data['country'], $id);
        update_field('team', $data['team'], $id);
        update_field('race', $data['race'], $id);
        update_field('link', $data['link'], $id);
        update_field('twitter_url', $data['twitter_url'], $id);
        update_field('fan_page', $data['fan_page'], $id);
    }

    // download the media file and assign as feature item
    _log("Loading progamer image: " . $img);
    $media = media_sideload_image($img, $id, $data['name']);
    if(!empty($media) && !is_wp_error($media)){
        $args = array(
            'post_type' => 'attachment',
            'posts_per_page' => -1,
            'post_status' => 'any',
            'post_parent' => $id
        );

        // reference new image to set as featured
        $attachments = get_posts($args);

        if(isset($attachments) && is_array($attachments)){
            foreach($attachments as $attachment) {
                // grab source of full size images (so no 300x150 nonsense in path)
                $image = wp_get_attachment_image_src($attachment->ID, 'full');
                // determine if in the $media image we created, the string of the URL exists
                if(strpos($media, $image[0]) !== false){
                    // if so, we found our image. set it as thumbnail
                    set_post_thumbnail($id, $attachment->ID);
                    // only want one image
                    break;
                }
            }
        }
    } else if (is_wp_error($media)) {
        _log("Failed." . $media->get_error_message());
        $msg = $media->get_error_message();
    }
    if (!$old_progamer) {
        sc2_save_obj_to_mongo($id);
        add_action('save_post', 'sc2_save_obj_to_mongo');
    }

    return array(
        'id'        => $id,
        'action'    => $action,
        'data'      => array ('msg' => $msg),
    );
}
function sc2_get_obj( $id ) {
	global $wp_json_posts;
	$id = (int) $id;

	if ( empty( $id ) ) {
		return new WP_Error( 'json_post_invalid_id', __( 'Invalid post ID.' ), array( 'status' => 404 ) );
	}

	$post = get_post( $id, ARRAY_A );

	if ( empty( $post['ID'] ) ) {
		return new WP_Error( 'json_post_invalid_id', __( 'Invalid post ID.' ), array( 'status' => 404 ) );
	}

	// get meta data
	$meta = $wp_json_posts->get_all_meta($id);

	$obj = array (
		'id'            => $id,
		'name'          => $post['post_title'],
		'status'        => $post['post_status'],
	);
	foreach ($meta as $row) {
		$obj[$row['key']] = $row['value'];
	}

	// get image
	$image_id = get_post_thumbnail_id($id);
	if (!empty($image_id)) {
		$obj['image'] = wp_get_attachment_metadata($image_id);
	} else {
		$obj['image'] = false;
	}

	return $obj;
}

function get_post_image($post_id) {
	// get image
	$image_id = get_post_thumbnail_id($post_id);
	if (!empty($image_id)) {
		return wp_get_attachment_metadata($image_id);
	}

	return false;
}

function sc2_get_field($field, $id) {
	global $wpdb;
	$sql = $wpdb->prepare("
		SELECT meta.meta_value
		FROM $wpdb->postmeta meta
		INNER JOIN $wpdb->posts p
		ON meta.post_id=p.ID
		AND meta.post_id =%d
		AND meta.meta_key=%s
	", $id, $field);
	// $wpdb->show_errors();
	$result = $wpdb->get_row($sql, ARRAY_N);

	if (empty($result)) {
		return false;
	}
	return $result[0];
}

/**
 * Copy this function from WP lib for better runtime performance
 * @param $id
 * @return array
 */
function sc2_get_all_meta($id) {
	global $wpdb;
	$table = _get_meta_table( 'post' );
	$results = $wpdb->get_results( $wpdb->prepare( "SELECT meta_id, meta_key, meta_value FROM $table WHERE post_id = %d", $id ) );

	$meta = array();

	foreach ( $results as $row ) {
		$value = sc2_prepare_meta( $row, true );

		if ( is_wp_error( $value ) ) {
			continue;
		}

		$meta[$value['key']] = $value['value'];
	}
	return $meta;
}

/**
 * Copy this function from WP lib for better runtime performance
 * @param $id
 * @return array
 */
function sc2_prepare_meta($data, $is_raw = false ) {
	$key   = $data->meta_key;
	$value = $data->meta_value;

	// Don't expose protected fields.
	if ( is_protected_meta( $key ) ) {
		return new WP_Error( 'json_meta_protected', sprintf( __( '%s is marked as a protected field.'), $key ), array( 'status' => 403 ) );
	}

	// Normalize serialized strings
	if ( $is_raw && is_serialized( $value ) ) {
		$value = unserialize( $value );
	}

	// Don't expose serialized data
	if ( is_serialized( $value ) || (! is_string( $value ) && !is_array($value) )) {
		return new WP_Error( 'json_meta_protected', sprintf( __( '%s contains serialized data.'), $key ), array( 'status' => 403 ) );
	}

	return array(
		'key'   => $key,
		'value' => $value,
	);
}

function sc2_create_upload_task($filename, $tmpFile, $event) {
	// insert new upload task
	$post = array(
		'post_title'            => $filename,
		'post_type'             => 'upload_task',
		'post_status'           => 'draft',
	);

	$id = wp_insert_post($post);

	if ($event != 0) {
		update_post_meta($id, 'event', $event);
	}


	$url = REPLAY_PARSER_URL;
	$data = array(
		"file"      => Unirest::file($tmpFile),
		"taskId"    => $id,
		"fileName"  => $filename,
	);
	$response = Unirest::post($url,
		array("Accept" => "application/json"),
		$data
	);

	if ($response->code != 200) {
		echo $response->body;
		return false;
	}

	return array(
		'ID'        => $id,
		'data'      => $data
	);
}

$mongo_post_types = array('draft', 'publish', 'trash');
function sc2_save_obj_to_mongo($post_id) {
    global $mongo_post_types;
    _log("Begining to save mongo obj.  Id:" . $post_id);

    // Autosave, do nothing
    if ( defined( 'DOING_AUTOSAVE' ) && DOING_AUTOSAVE )
        return;

    $post = get_post($post_id, "ARRAY_A");
    $post_type = $post['post_type'];

    // only accept
    if (!in_array($post['post_status'], $mongo_post_types)) {
        return false;
    }

    if ($post['post_status'] == 'trash') {
        return sc2_do_delete_obj_from_mongo($post['post_type'], $post_id);
    }
    $post = sc2_get_object_for_mongo($post);
    if (!$post)
        return;

    if ($post_type == 'player' && array_key_exists('url', $post['object'])) {
        $post['object']['url'] = strtolower($post['object']['url']);
    }

    $url = MONGO_DB_API_URL;
    $data = array(
        "post_type"      => $post['post_type'],
        "object"        => json_encode($post['object'])
    );
    _log("Sending rest request");
    _log($data);
    $response = Unirest::post($url,
        array(),
        $data
    );

    if ($response->code != 200) {
        _log("Error occurred. Message" . $response->body);
        return false;
    }
    return true;
}

$obj_matrix = array(
    "event"         => ['link', 'start_date', 'description', 'parent'],
    "map"           => ['hash', 'server', 'url'],
    "player"        => ['url', 'toon_handle', 'toon_id', 'region', 'subregion', 'progamer'],
    "progamer"      => ['url', 'native_name', 'romanized_name', 'country', 'race', 'team', 'birthday', 'twitter_url', 'fan_page'],
    "upload_task"   => ['event', 'message'],
);
$bypassed_titles = array('Auto Draft');
function sc2_get_object_for_mongo($post) {
    global $obj_matrix, $bypassed_titles;
    if (in_array($post['post_title'], $bypassed_titles))
        return false;

    $post_type = $post['post_type'];
    if (!array_key_exists($post_type, $obj_matrix))
        return false;

    $post_id = $post['ID'];
    // get meta data
    _log('Getting meta data');
    $meta_data = sc2_get_all_meta($post_id);
    $obj = array('name' => $post['post_title'], '_id' => $post_id);
    foreach ($meta_data as $key => $value) {
        if (in_array($key, $obj_matrix[$post_type])) {
            $obj[$key] = $value;
        }
    }

    _log('Getting image info');
    $imageInfo = get_post_image($post_id);
    if ($imageInfo)
        $obj['image'] = $imageInfo;

    // handle parent
    if ($post_type == 'player' && array_key_exists('progamer', $obj)) {
        $obj['progamer'] = $obj['progamer'][0];
    } else if ($post_type == 'upload_task' && array_key_exists('event', $obj)) {
        $obj['event_id'] = intval($obj['event']);
        unset($obj['event']);
    }
    return array("post_type" => $post_type, "object" => $obj);
}

function sc2_do_delete_obj_from_mongo($post_type, $post_id) {
    _log("Deleting obj from mongo");
    $url = MONGO_DB_API_URL . "?post_type=" . $post_type . "&id=" . $post_id;
    $response = Unirest::delete($url);
    if ($response->code != 200) {
        _log("Failed. Message:" . $response->body);
        return false;
    }
    return true;

}

if ( ! function_exists( 'sc2_get_posts_by_category' ) ) :
    function sc2_get_posts_by_category($category) {
        $args = array(
            'posts_per_page'   => 5000,
            'offset'           => 0,
            'category'         => '',
            'category_name'    => $category,
            'include'          => '',
            'exclude'          => '',
            'meta_key'         => '',
            'meta_value'       => '',
            'post_type'        => 'post',
            'post_mime_type'   => '',
            'post_parent'      => '',
            'post_status'      => 'publish',
            'suppress_filters' => true );
        $posts =  get_posts($args);
        $result = array();
        foreach ($posts as $post) {
            $p = array(
                'ID'        => $post->ID,
                'title'     => $post->post_title,
                'content'   => $post->post_content,
                'link'      => get_field('link', $post->ID),
                'image'     => get_post_image($post->ID)
            );
            $result[] = $p;
        }
        return $result;
    }
endif; // sc2_get_posts_by_category


if ( ! function_exists( 'sc2_get_posts_by_id' ) ) :
    function sc2_get_posts_by_id($id) {

        $post =  get_post($id);
        $result = array(
            'ID'        => $post->ID,
            'title'     => $post->post_title,
            'content'   => $post->post_content,
            'image'     => get_post_image($id)
        );

        return $result;
    }
endif; // sc2_get_posts_by_id