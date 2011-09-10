#!/bin/bash

pushd .
cd `echo $0 | sed "s/importDBChangeset.sh//"`
ant -f ./build.xml -propertyfile $1 importLiquiBaseChangeSet
popd