cd cls_sbt
SBT_OPTS="-Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled"
java $SBT_OPTS -jar ~/bin/sbt-launch.jar \
  compile "run-main AppCreator" \
  && ionic serve
cd ..
