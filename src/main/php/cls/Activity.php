<?php
final class Activity {
	private $user_id;
	public function __construct($activity, $user_id) {
		$this->name = $activity['activity_name'];
		$this->accuracy = $activity['recognition_accuracy'];
		$this->end = $activity['recognition_date'];
		$this->start = $this->end;
		$this->user_id = $user_id;
	}
	public function log() {
		$sql = "INSERT INTO activities (user_id, activity_name, recognition_accuracy, recognition_date) VALUES (:user_id, :activity_name, :recognition_accuracy, :recognition_date)";
		$stmt = Database::get()->prepare($sql);
		$stmt->bindValue(":user_id", $this->user_id);
		$stmt->bindValue(":activity_name", $this->name);
		$stmt->bindValue(":recognition_accuracy", $this->accuracy);
		$stmt->bindValue(":recognition_date", $this->end);
		$stmt->execute();
	}
}
