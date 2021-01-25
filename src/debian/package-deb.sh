#!/bin/bash

echo "Initialising .deb packaging.."

PACKAGE_NAME="search-note"
PACKAGE_VERSION="1.0.0"
BASE=$PACKAGE_NAME-$PACKAGE_VERSION

mkdir -p src/debian/$BASE/DEBIAN
mkdir -p src/debian/$BASE/usr/bin
mkdir -p src/debian/$BASE/usr/share/applications
mkdir -p src/debian/$BASE/usr/share/applications
mkdir -p src/debian/$BASE/usr/share/doc/$PACKAGE_NAME
mkdir -p src/debian/$BASE/usr/share/java
mkdir -p $PWD/src/debian/temp

cp $PWD/src/debian/search-note.desktop src/debian/$BASE/usr/share/applications/
cp $PWD/src/debian/copyright src/debian/$BASE/usr/share/doc/$PACKAGE_NAME
cp $PWD/src/main/resources/images/search-note.png src/debian/$BASE/usr/share/doc/$PACKAGE_NAME
cp $PWD/target/$BASE.jar src/debian/$BASE/usr/share/java/

echo "Package: $PACKAGE_NAME" > src/debian/$BASE/DEBIAN/control
echo "Version: $PACKAGE_VERSION" >> src/debian/$BASE/DEBIAN/control
cat $PWD/src/debian/control >> src/debian/$BASE/DEBIAN/control
echo "$PACKAGE_NAME ($PACKAGE_VERSION) unstable; urgency=low" > $PWD/src/debian/temp/changelog
cat $PWD/src/debian/changelog >> $PWD/src/debian/temp/changelog
gzip -9c $PWD/src/debian/temp/changelog > src/debian/$BASE/usr/share/doc/$PACKAGE_NAME/changelog.gz

chmod 644 src/debian/$BASE/usr/share/doc/$PACKAGE_NAME/search-note.png
echo '#!/bin/bash' > src/debian/$BASE/usr/bin/search-note
echo "java -jar /usr/share/java/$BASE.jar" >> src/debian/$BASE/usr/bin/search-note
chmod 755 src/debian/$BASE/usr/bin/search-note

dpkg-deb --root-owner-group -b ./src/debian/$BASE ./src/debian/${BASE}_amd64.deb

rm -r $PWD/src/debian/temp
rm -r $PWD/src/debian/$BASE

echo ".deb packaging complete"

