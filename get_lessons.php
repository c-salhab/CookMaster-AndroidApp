<?php

session_start();

require "DataBase.php";

$db = new DataBase();
if (isset($_SESSION['email'])) {
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
} else {
    echo "User is not logged in";
}