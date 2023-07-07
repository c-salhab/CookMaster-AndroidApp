<?php
session_start();

require "DataBase.php";

$db = new DataBase();

if ($db->dbConnect()) {
    if (isset($_SESSION['id'])) {
        $userId = $_SESSION['id'];
        $subscriptionId = $db->getSubscription($userId);

        if ($subscriptionId !== null) {
            echo $subscriptionId;
        } else {
            echo "No subscription found.";
        }
    } else {
        echo "User not logged in.";
    }
} else {
    echo "Error: Database connection";
}

