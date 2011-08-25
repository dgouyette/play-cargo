package play.modules.cargo;

import org.codehaus.cargo.container.Container;
import org.codehaus.cargo.container.ContainerType;
import org.codehaus.cargo.container.configuration.RuntimeConfiguration;
import org.codehaus.cargo.container.deployable.Deployable;
import org.codehaus.cargo.container.deployable.WAR;
import org.codehaus.cargo.container.deployer.DeployableMonitor;
import org.codehaus.cargo.container.deployer.Deployer;
import org.codehaus.cargo.container.deployer.DeployerType;
import org.codehaus.cargo.container.tomcat.TomcatRuntimeConfiguration;
import org.codehaus.cargo.generic.DefaultContainerFactory;
import org.codehaus.cargo.generic.deployer.DefaultDeployerFactory;
import play.Play;



public class Cargo {
	
	
	
    public static void main(String [] args) throws Exception {
    	
    	if (args.length !=2){
    		throw new IllegalArgumentException("Not enougth parameters, expected 2 got "+args.length);
    	}
    	
    	String warPath = args[0];
    	
    	if (warPath==null){
    		throw new IllegalArgumentException("War Path is null");
    	}
    	
    	String command = args[1];
    	
    	if (command==null){
    		throw new IllegalArgumentException("command is null");
    	}
    	
    	Play.readConfiguration();
    	
    	
    	String username = Play.configuration.getProperty("cargo.remote.username");
    	String password = Play.configuration.getProperty("cargo.remote.password");
    	String url =	Play.configuration.getProperty("cargo.tomcat.manager.url");
 
    	System.out.println("--------------");
    	System.out.printf("-> %s", command);
    	System.out.println("\n--------------");
    	    	
    	Deployable war = new WAR(warPath);
    	
        RuntimeConfiguration runtimeConfiguration = new TomcatRuntimeConfiguration();
        runtimeConfiguration.setProperty("cargo.remote.username", username);
        runtimeConfiguration.setProperty("cargo.remote.password", password);
        runtimeConfiguration.setProperty("cargo.tomcat.manager.url", url);

        Container container = new DefaultContainerFactory().createContainer("tomcat6x", ContainerType.REMOTE, runtimeConfiguration);

        Deployer deployer = new DefaultDeployerFactory().createDeployer(container, DeployerType.REMOTE);
        
        //DeployableMonitor monitor;
    	
        deployer.
        if (command.equals("deploy")){
        	deployer.deploy(war);
        }
        else if(command.equals("undeploy")){
        	deployer.undeploy(war);
        }else if (command.equals("redeploy")){
        	deployer.redeploy(war);
        }
        
        System.out.println("--------------");
    	System.out.printf("-> %s completed", command);
    	System.out.println("\n--------------");
        
    	
    }
    	

}