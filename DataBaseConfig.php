<?php

class DataBaseConfig
{
    public $servername;
    public $username;
    public $password;
    public $databasename;

    public function __construct()
    {
        $this->servername = 'yourcookmaster.com';
        $this->username = 'forge';
        $this->password = 'E4jAlIstBZFrd5rIjG5p';
        $this->databasename = 'cook_master';
    }
}