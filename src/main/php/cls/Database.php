<?php
final class Database {
	public static $database;
	public static function get() {
		if (static::$database === null) {
			$config = Config::get()['database'];
			try {
				$db_object = new PDO("mysql:host={$config['host']};dbname={$config['name']}", $config['user'], $config['pass']);
				$db_object->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
				static::$database = $db_object;
			} catch (PDOException $e) {
				$message = "database connection failed: ".$e->getMessage();
				error_log($message);
				throw new Exception($message);
			}
		}
		return static::$database;
	}
}
