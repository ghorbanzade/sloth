<?php
spl_autoload_register('myAutoloader');
function myAutoloader($class_name) {
	include("cls/$class_name.php");
}
require 'vendor/autoload.php';

$config = Config::get('sloth');
$database = Database::get();

function get_user_by_token($token) {
	$sql = "SELECT user_id, firstname, lastname FROM users WHERE private_token = :private_token";
	$stmt = Database::get()->prepare($sql);
	$stmt->bindValue(":private_token", $token);
	$stmt->execute();
	$res = $stmt->fetch(PDO::FETCH_ASSOC);
	return new User($res);
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
	$data = json_decode(file_get_contents("php://input"), true);
	$user = get_user_by_token($data['user']['token']);
	$activities = array();
	foreach ($data['activities'] as &$act) {
		$activity = new Activity($act, $user->get_id());
		$activity->log();
	}
} else {
	$token = 'NT20p6xPrdDLeBYs';
	$user = get_user_by_token($token);
	$response = array(
		'user' => $user,
		'activities' => $user->get_activities(),
		'metrics' => $user->get_metrics()
	);
	header("Content-Type: application/json");
	echo json_encode($response, true);
}
