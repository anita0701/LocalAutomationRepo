rm /var/lib/jenkins/workspace/build-automation/test-report/screenshots/*
cd /var/www/reports
dt_prev=$(date -d "`date`-7days" +%Y-%m-%d)
rm -R /var/www/reports/$dt_prev
