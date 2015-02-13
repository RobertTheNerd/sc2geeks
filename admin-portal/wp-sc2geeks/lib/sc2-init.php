<?php
/**
 * Created by PhpStorm.
 * User: robert
 * Date: 8/12/14
 * Time: 6:37 PM
 */
include_once( dirname( __FILE__ ) . '/sc2lib.php' );


function sc2_custom_post_status(){
	register_post_status( 'fail', array(
		'label'                     => _x( 'Failed', 'post' ),
		'public'                    => true,
		'exclude_from_search'       => false,
		'show_in_admin_all_list'    => true,
		'show_in_admin_status_list' => true,
		'label_count'               => _n_noop( 'Failed <span class="count">(%s)</span>', 'Failed <span class="count">(%s)</span>' ),
	) );
}
add_action( 'init', 'sc2_custom_post_status' );

function sc2_custom_upload_mimes ( $existing_mimes=array() ) {
	// add your extension to the mimes array as below
	$existing_mimes['zip'] = 'application/zip';
	$existing_mimes['gz'] = 'application/x-gzip';
	return $existing_mimes;
}
add_filter('upload_mimes', 'sc2_custom_upload_mimes');

function sc2_add_query_vars_filter( $vars ){
	$vars[] = "ids";
	return $vars;
}
add_filter( 'query_vars', 'sc2_add_query_vars_filter' );

// handles both post update & deletion (status -> 'trash')
add_action('save_post', 'sc2_save_obj_to_mongo');
