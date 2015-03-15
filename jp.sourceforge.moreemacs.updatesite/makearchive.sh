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

fail() {
    echo "$1"
    exit 1
}

version=$1
name=moreemacs_$version
mkdir $name
if ! cp -r $files $name; then
   fail "copy files"
fi
if ! zip -qr $name.zip $name; then
   fail "can't make an archive"
fi
rm -rf $name
