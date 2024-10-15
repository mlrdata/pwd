@echo off
set java_port=35589
for /f "tokens=1-5" %%i in ('netstat -ano^|findstr ":%java_port%"') do taskkill /pid %%m -t -f
exit