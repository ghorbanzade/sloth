<?php
final class Activity {
	public function __construct($activity) {
		$this->name = $activity['activity_name'];
		$this->accuracy = $activity['recognition_accuracy'];
		$this->end = $activity['recognition_date'];
		$this->start = $this->end;
	}
	public function setStartTime($time) {
		$this->start = $time;
	}
}
