<?php

require "DataBase.php";

$db = new DataBase();

if ($db->dbConnect()) {
    if (isset($_GET['email'])) {
        $email = $_GET['email'];
        $userInfo = $db->getInfosUser($email);

        if ($userInfo !== null) {
            echo json_encode($userInfo);
        } else {
            echo "User not found.";
        }
    } else {
        echo "Missing email parameter.";
    }
} else {
    echo "Error: Database connection";
}