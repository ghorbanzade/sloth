<?php
final class User {
	private $uid;
	public function __construct($user) {
		$this->uid = $user['user_id'];
		$this->first = $user['firstname'];
		$this->last = $user['lastname'];
	}
	public function get_activities() {
		$sql = "SELECT activity_name, recognition_accuracy, recognition_date FROM activities WHERE user_id = :user_id ORDER BY recognition_date DESC LIMIT 0, 20";
		$stmt = Database::get()->prepare($sql);
		$stmt->bindValue(":user_id", $this->uid);
		$stmt->execute();
		$activities = array();
		while ($res = $stmt->fetch(PDO::FETCH_ASSOC)) {
			array_push($activities, new Activity($res, $this->uid));
		}
		return $activities;
	}
	public function get_metrics() {
		$sql = "SELECT M.sensor_id, S.sensor_name, M.status, R.report_date FROM (SELECT sensor_id, MAX(report_date) as report_date FROM metrics GROUP BY sensor_id) R, metrics M, sensors S WHERE S.sensor_id = M.sensor_id AND M.sensor_id = R.sensor_id AND M.report_date = R.report_date AND S.user_id = :user_id ORDER BY M.sensor_id ASC";
		$stmt = Database::get()->prepare($sql);
		$stmt->bindValue(":user_id", $this->uid);
		$stmt->execute();
		$metrics = array();
		while ($res = $stmt->fetch(PDO::FETCH_ASSOC)) {
			array_push($metrics, new SensorMetric($res));
		}
		return $metrics;
	}
	public function get_id() {
		return $this->uid;
	}
}
