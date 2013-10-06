#!/bin/bash

make

if [ $? -eq 0 ]
then
    echo 'jar cf mazegen_plugin.jar com plugin.yml'
    jar cf mazegen_plugin.jar com plugin.yml
fi

if [ $? -eq 0 ]
then
    echo 'mv mazegen_plugin.jar test_world/plugins/'
    mv mazegen_plugin.jar test_world/plugins/
fi
