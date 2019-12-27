cd /var/lib/jenkins/workspace/build-automation/
curr_date=$(date '+%Y-%m-%d')
mkdir /var/www/reports/$curr_date
cp -r test-report/screenshots/ /var/www/reports/$curr_date
cp test-report/TestReport.html /var/www/reports/$curr_date


