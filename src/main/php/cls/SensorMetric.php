<?php
final class SensorMetric {
	private $sid;
	public function __construct($res) {
		$this->sid = $res['sensor_id'];
		$this->name = $res['sensor_name'];
		$this->status = ($res['status'] == 1) ? true : false;
		$this->last_update = $res['report_date'];
	}
}
