<?php
require "DataBaseConfig.php";

class DataBase
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    function logIn($table, $name, $password)
    {
        $name = $this->prepareData($name);
        $password = $this->prepareData($password);
        $this->sql = "SELECT * FROM " . $table . " WHERE email = '" . $name . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            $dbname = $row['email'];
            $dbpassword = $row['password'];
            if ($dbname == $name && password_verify($password, $dbpassword)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    function getSubscriptionByEmail($email) {
        $email = $this->prepareData($email);
        $this->sql = "SELECT subscription_id FROM users WHERE email = '" . $email . "'";
        $result = mysqli_query($this->connect, $this->sql);

        if (mysqli_num_rows($result) > 0) {
            $row = mysqli_fetch_assoc($result);
            $subscriptionId = $row['subscription_id'];
            return $subscriptionId;
        }

        return null;
    }

    function getInfosUser($email) {
        $email = $this->prepareData($email);
        $this->sql = "SELECT users.first_name, users.last_name, users.subscription_id, subscriptions.name AS subscription_name FROM users INNER JOIN subscriptions ON users.subscription_id = subscriptions.id WHERE users.email = '" . $email . "'";
        $result = mysqli_query($this->connect, $this->sql);

        if (mysqli_num_rows($result) > 0) {
            $row = mysqli_fetch_assoc($result);
            $userInfo = array(
                'first_name' => $row['first_name'],
                'last_name' => $row['last_name'],
                'subscription_id' => $row['subscription_id'],
                'subscription_name' => $row['subscription_name']
            );
            return $userInfo;
        } else {
            return null;
        }
    }

    function getUserFormations($email) {
        $email = $this->prepareData($email);
        $this->sql = "SELECT classes.id, classes.title, possess_classes.url_to_exam, possess_classes.date_of_exam FROM possess_classes INNER JOIN classes ON possess_classes.class_id = classes.id WHERE possess_classes.user_id = (SELECT id FROM users WHERE email = '$email')";
        $result = mysqli_query($this->connect, $this->sql);

        $userFormations = [];
        if ($result && mysqli_num_rows($result) > 0) {
            while ($row = mysqli_fetch_assoc($result)) {
                $userFormations[] = [
                    'id' => $row['id'],
                    'title' => $row['title'],
                    'url_to_exam' => $row['url_to_exam'],
                    'date_of_exam' => $row['date_of_exam']
                ];
            }
        }

        return $userFormations;
    }

}