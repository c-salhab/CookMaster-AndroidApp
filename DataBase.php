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
                $login = true;
            } else {
                $login = false;
            }
        } else {
            $login = false;
        }

        return $login;
    }

    function getLessons()
    {
        $this->sql = "SELECT * FROM users";
        $result = mysqli_query($this->connect, $this->sql);

        $lessons = array();

        if (mysqli_num_rows($result) > 0) {
            while ($row = mysqli_fetch_assoc($result)) {
                $lessons[] = $row;
            }
        }

        return $lessons;
    }

    function getSubscription($userId) {
        $this->sql = "SELECT subscription_id FROM users WHERE id = " . $userId;
        $result = mysqli_query($this->connect, $this->sql);

        if (mysqli_num_rows($result) > 0) {
            $row = mysqli_fetch_assoc($result);
            $subscriptionId = $row['subscription_id'];
            return $subscriptionId;
        }

        return null;
    }
}