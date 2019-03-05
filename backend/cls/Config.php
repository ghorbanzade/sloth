<?php
final class Config {
	private static $config;
	public static function get() {
		if (static::$config === null) {
			$content = file_get_contents("config/sloth.json");
			static::$config = json_decode($content, true);
		}
		return static::$config;
	}
}
