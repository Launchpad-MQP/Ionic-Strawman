cd cls_sbt
SET SBT_OPTS=-Xms512M -Xmx512M -Xss1M -XX:+CMSClassUnloadingEnabled
java %SBT_OPTS% -jar "C:\Program Files (x86)\sbt\bin\sbt-launch.jar"^
  compile "run-main AppCreator" ^
  && ionic serve
cd ..