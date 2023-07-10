<?php

require "DataBase.php";

$db = new DataBase();

if ($db->dbConnect()) {

    $email = $_GET['email'];
    $formations = $db->getUserFormations($email);

    if (!empty($formations)) {
        echo json_encode($formations);
    } else {
        echo "No formations found.";
    }

} else {
    echo "Error: Database connection";
}
