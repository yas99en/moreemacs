#!/bin/sh
if [ $#  -lt 1 ]; then
    echo "usage: $0 version"
    echo " ex) \$ $0 2.0.0"
    exit
fi

files="artifacts.jar content.jar features plugins site.xml"

for file in $files; do
    if [ ! -e $file ]; then
        echo "$file not found"
        exit
    fi
done

version=$1
name=moreemacs_$version
mkdir $name
cp -r $files $name
zip -r $name.zip $name
rm -rf $name
