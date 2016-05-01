<?php
spl_autoload_register('myAutoloader');
function myAutoloader($class_name) {
        include("cls/$class_name.php");
}
require 'vendor/autoload.php';

$config = Config::get('sloth');
$database = Database::get();

$token = "your-token"; // this is going to be posted with user request

function get_user_by_token($token) {
	$sql = "SELECT user_id, firstname, lastname FROM users WHERE private_token = :private_token";
	$stmt = Database::get()->prepare($sql);
	$stmt->bindValue(":private_token", $token);
	$stmt->execute();
	$res = $stmt->fetch(PDO::FETCH_ASSOC);
	return new User($res);
}


$user = get_user_by_token($token);
$response = array(
	'user' => $user,
	'activities' => $user->get_activities(),
	'metrics' => $user->get_metrics()
);

print_r(json_encode($response, true));
