<?php
final class Database {
	public static $database;
	public static function get() {
		if (static::$database === null) {
			$config = Config::get()['database'];
			try {
				$db = new PDO("mysql:host={$config['host']};dbname={$config['name']}", $config['user'], $config['pass']);
				$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
				static::$database = $db;
			} catch (PDOException $e) {
				error_log("database connection failed: ".$e->getMessage());
				die();
			}
		}
		return static::$database;
	}
}
