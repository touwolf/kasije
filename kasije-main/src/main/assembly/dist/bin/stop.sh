#!/bin/sh

function realpath
{
     echo $(cd $(dirname "$1"); pwd)/$(basename "$1");
}

__FILE__=`realpath "$0"`
__DIR__=`dirname "${__FILE__}"`

cd "{__DIR__}/.."
bin/kasije.sh stop
