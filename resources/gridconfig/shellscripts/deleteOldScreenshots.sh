rm -r /home/anita/eclipse-workspace/TS_Automation_project/automation-scripts/resources/screenshots/*
cd /home/anita/eclipse-workspace/TS_Automation_project/automation-scripts/resources/
now=$(date '+%Y_%m_%d')
mkdir $now
cp -r screenshots/ $now/

