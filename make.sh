#!/bin/sh

git pull
sudo ant -f ./build-dist.xml -propertyfile ./build-dist-local.properties clean
mvn clean install -DskipTests=true -P production
ant -f ./build-dist.xml -propertyfile ./build-dist-local.properties make import-changeset
sudo ant -f ./build-dist.xml -propertyfile ./build-dist-local.properties deploy deploy-config

sudo rm -fr /var/log/tomcat7/*
sudo rm -fr /var/log/nginx/*

sudo /etc/init.d/tomcat7 restart
sudo /etc/init.d/nginx restart
