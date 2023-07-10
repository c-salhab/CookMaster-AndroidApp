<?php
require "DataBase.php";

$db = new DataBase();

if ($db->dbConnect()) {

    $email = $_GET['email'];
    $subscriptionId = $db->getSubscriptionByEmail($email);
    echo $subscriptionId;
}