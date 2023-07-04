<?php
require "DataBase.php";

$db = new DataBase();

if ($db->dbConnect()) {
    $lessons = $db->getLessons();
    if (!empty($lessons)) {

        echo json_encode($lessons);
    } else {
        echo "No lessons found.";
    }
} else {
    echo "Error: Database connection";
}
