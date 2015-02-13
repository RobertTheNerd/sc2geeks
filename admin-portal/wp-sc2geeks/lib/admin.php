<?php
/**
 * Created by PhpStorm.
 * User: robert
 * Date: 8/5/14
 * Time: 5:37 PM
 */
include_once( dirname( __FILE__ ) . '/sc2lib.php' );
include_once( dirname( __FILE__ ) . '/admin-progamer.php' );


// create custom plugin settings menu
function sc2_add_menu_items() {
	//create custom post type menu
	add_menu_page( 'Replay Management',  'Replay Management', 'manage_options', 'sc2_replays', 'sc2_upload' );

	//create submenu items
    add_submenu_page( 'sc2_replays', 'Upload Replay', 'Upload Replay', 'manage_options', 'sc2_replays_upload', 'sc2_upload' );
    add_submenu_page( 'edit.php?post_type=progamer', 'Progamer Mapping', 'Progamer Mapping', 'manage_options', 'sc2_progamer_mapping', 'sc2_progamer_mapping' );
}
add_action( 'admin_menu', 'sc2_add_menu_items' );


function sc2_upload() {
    ?>
    <link href="<?php echo get_stylesheet_directory_uri() ?>/css/bootstrap.css" rel="stylesheet"/>
    <div class="wrap">
        <h2>Replay upload</h2>
        <hr style="margin: 2px 0 10px">
        <?php
	if ($_SERVER['REQUEST_METHOD'] == 'GET'):
        $cookie = $_COOKIE['wordpress_logged_in_3a14e3291761486941e826f36401d330'];

        $result = wp_validate_auth_cookie($cookie);
        _log($result);
        global $current_user;
        $current_user = [];
        $current_user = wp_get_current_user();
        if (!($current_user instanceof WP_User)) {
            $response = json_ensure_response( $this->get_json_response(array()) );
            $response->set_status(403);
        }
        ?>


        <form action="<?php echo sc2_get_uploadurl() ?>" method="post"
              enctype="multipart/form-data">
            <div class="form-group">
                <label for="file">File Name:</label>
                <input type="file" name="file" id="file"><br>
            </div>

            <div class="form-group">
                <Label for="sc2_event">Tournament</Label><?php sc2_admin_get_events() ?>
                <p class="help-group">Please note: only <i>.SC2Replay</i> and <i>.zip</i> files are supported. Others will be rejected.</p>
            </div>
            <input type="submit" name="submit" value="Submit" class="btn btn-default">
        </form>
<?php
	else:
		if (! array_key_exists('file', $_FILES)){ ?>
			<div class='error'>No file is specified for upload </div>
		<?php } else {
			$file = $_FILES['file'];
			if ($file['error'] > 0 ) { ?>
				<div class='error'>Error occurred while uploading: <?php echo $file['error'] ?></div>
			<?php } else {
				// only allow SC2Replay & 'zip' file types
				$ext = strtoupper(pathinfo($file['name'], PATHINFO_EXTENSION));
				if ($ext != 'SC2REPLAY'
					&& $ext != 'ZIP') {
					?>
					<div class="error">Specified file type is not supported. Only .SC2Replay and .zip files are supported.</div>
					<?php
				} else {
					// create a new task
					$task = sc2_create_upload_task($file['name'], $file['tmp_name'], $_POST['event']);

					if (!$task) {
						?>
						<div class="error">Failed to create task for the file specified.</div>
<?php
					} else {
						?>
						<style>
							.sc2_msg_title {
								display: inline-block;
								width: 150px;
							}
							#sc2_progress li div {
								display: inline-block;
							}
							#sc2_progress li progress {
								width: 300px;
							}


						</style>
						<div>
							<p class="bg-success">Task created for file <?php echo $file['name'] ?>.</p>
							<div id="summary">Processing ...</div>
							<ul id="sc2_progress">
								<li>
									<label class="sc2_msg_title">Current File:</label>
									<div id="sc2_current_file"></div>
								</li>
								<li>
									<label class="sc2_msg_title">Current Action:</label>
									<div id="sc2_current_action"></div>
								</li>
								<li>
									<label class="sc2_msg_title">Current Progress:</label>
									<progress id="sc2_current_progress" max="100" min="0" value="0"></progress>
								</li>
								<li>
									<label class="sc2_msg_title">Total Progress:</label>
									<progress id="sc2_total_progress" max="100" min="0" value="0"></progress>
								</li>
								<li>
									<label class="sc2_msg_title">Message:</label>
									<div id="sc2_message"></div>
								</li>
							</ul>
						</div>
						<script>

							String.prototype.endsWith = function(suffix) {
								return this.indexOf(suffix, this.length - suffix.length) !== -1;
							};

							var sc2_url = '<?php echo sc2_get_rest_url() . '/' . $task['ID'] ?>';
							var sc2_status_time;
							var totalTime = 0;
							var interVal = 200;
							var timeoutForError = 60*1000;
							var lastTotalProgress = 0;
							var sc2_response_fields = ['current_file', 'current_action',
								'current_progress', 'total_progress', 'message'];
							function refresh_status() {

								totalTime += 200;
								var currentTotalProgress = lastTotalProgress;

								// update status
								jQuery.getJSON(sc2_url, function(data){
									for (var i = 0; i < sc2_response_fields.length; i ++) {
										var field = sc2_response_fields[i];
										if (field in data) {
											if (field.endsWith('progress')) {
												jQuery('#sc2_'+field).val(data[field]);
											} else {
												jQuery('#sc2_'+field).text(data[field]);
											}
										}
									}
									var field = 'total_progress';
									var status = data['status'];
									if (status == 'publish' || status == 'fail') {
										if (status == 'publish')
											status = 'Successful';
										jQuery('#sc2_current_progress').val(100);
										jQuery('#sc2_total_progress').val(100);
										jQuery('#summary').text('File processed! Result: ' + status + '.');
										return;
									}

									if ('total_progress' in data)
										currentTotalProgress = data['total_progress'];

									if (totalTime >= timeoutForError) {
										if (currentTotalProgress <= lastTotalProgress) {
											// timeout and no change to TotalProgress. Assume it's dead.
											return;
										} else {
											lastTotalProgress = currentTotalProgress;
											totalTime = 0;
										}
									}
									setTimeout(refresh_status, interVal);

								});
							}

							jQuery(window).load(refresh_status);

						</script>

<?php
					}


				}
			}


		}

	endif;
?>
    </div>
<?php
}

function sc2_get_uploadurl() {
	return admin_url( 'admin.php?page=sc2_replays_upload&return=1' );
}
function sc2_get_rest_url() {
	return get_site_url() . '/wp-json/sc2obj';
}

function sc2_admin_get_events() {
	global $wpdb;
	$events = $wpdb->get_results("
		SELECT ID, post_title
		FROM $wpdb->posts
		WHERE post_type='event'
		 AND post_status in ('publish', 'draft')
		ORDER BY post_title");
?>
	<select name="event" id="sc2_event">
		<option value="0">-SELECT-</option>
<?php
	if (isset($events) && !empty($events)) {
		foreach ($events as $event) {
		?>
			<option value="<?php echo $event->ID ?>"><?php echo $event->post_title . ' (' . $event->ID . ')' ?></option>
		<?php
		}
	}
?>
	</select>
<?php

}
