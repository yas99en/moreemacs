#!/bin/sh
if [ $#  -lt 1 ]; then
    echo "usage: $0 2.0.0"
    exit
fi

version=$1
name=moreemacs_$version
mkdir $name
cp -r artifacts.jar content.jar features plugins site.xml $name
zip -r $name.zip $name
rm -rf $name
