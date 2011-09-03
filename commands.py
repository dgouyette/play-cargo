import sys
import os
import getopt
import shutil
import subprocess
import tempfile
from optparse import OptionParser

try:
    from play.utils import package_as_war
    PLAY10 = False
except ImportError:
    PLAY10 = True

MODULE = 'cargo'

# Commands that are specific to your module

COMMANDS = ['cargo:deploy', 'cargo:redeploy','cargo:undeploy']



def execute(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")
	
    java_args = []
    cargo_args = []
    war_path =  generate_war(app, env, args)
    cargo_args.append("%s" % war_path)
	
    print "[command="+command+"]"
    if command == "cargo:deploy":
        cargo_args.append("deploy" )
    
    if command == "cargo:undeploy":
        cargo_args.append("undeploy")
       
    if command == "cargo:redeploy":
        cargo_args.append("redeploy")    
		
	
    java_cmd = app.java_cmd(java_args, None, "play.modules.cargo.Cargo", cargo_args)	
    
    try:
    	subprocess.call(java_cmd, env=os.environ)
    except OSError:
		print "Could not execute the java executable, please make sure the JAVA_HOME environment variable is set properly (the java executable should reside at JAVA_HOME/bin/java). "
		sys.exit(-1)
    	        

# This will be executed before any command (new, run...)
def before(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")


# This will be executed after any command (new, run...)
def after(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")

    if command == "new":
        pass


def generate_war(app, env, args):
	java_cmd = app.java_cmd(args)
    
	if os.path.exists(os.path.join(app.path, 'tmp')):
		shutil.rmtree(os.path.join(app.path, 'tmp'))
	if os.path.exists(os.path.join(app.path, 'precompiled')):
		shutil.rmtree(os.path.join(app.path, 'precompiled'))

	java_cmd.insert(2, '-Dprecompile=yes')
	try:
		result = subprocess.call(java_cmd, env=os.environ)
		if not result == 0:
			print "~"
			print "~ Precompilation has failed, stop deploying."
			print "~"
			sys.exit(-1)
	except OSError:
		print "error"
		sys.exit(-1)	
	
	war_path = os.path.join(tempfile.gettempdir(), os.path.basename(app.path))
	package_as_war(app, env, war_path, "%s.war" % war_path)
	return "%s.war" % war_path	
	
