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
            $subscriptionId = $row['subscription_id'];
            if ($dbname == $name && password_verify($password, $dbpassword)) {
                if ($subscriptionId === null) {
                    $showAds = true; // Afficher les publicités
                } else {
                    $showAds = false; // Ne pas afficher les publicités
                }
                $login = true;
            } else {
                $login = false;
            }
        }

        $response = array('login' => $login, 'showAds' => $showAds);
        return $response;

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

}