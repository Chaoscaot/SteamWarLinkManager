#!/bin/bash
parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
if [ -f ${iDir}/SteamWarLinkManager-new.jar" ];
then
    echo "New file found, replacing old file"
    java -jar ${iDir}/SteamWarLinkManager-new.jar install -i "${iDir}"
    rm ${iDir}/SteamWarLinkManager-new.jar
fi

java -jar $parent_path/SteamWarLinkManager.jar $@