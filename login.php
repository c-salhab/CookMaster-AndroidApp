<?php

require "DataBase.php";

$db = new DataBase();

if (isset($_POST['username']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        $loginResult = $db->logIn("users", $_POST['username'], $_POST['password']);
        $login = $loginResult[0];
        $showAds = $loginResult[1];

        if ($login) {
            echo "Login Success";
        } else {
            echo "Username or Password wrong";
        }
    } else {
        echo "Error: Database connection";
    }
} else {
    echo "All fields are required";
}
