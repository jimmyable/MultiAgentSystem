package cs551k.assessment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin Kollingbaum
 * University of Aberdeen
 * 2018
 *
 */

public class MAS
{
	// the whole MAS
	static private String[] agentList = {
			"???",
			"???",
			"???",
			"???"
	};
	
	static private String [][] taskhierarchy = {
			{"???", "???"}
	} ;

	
	static private String [][] agentCapabilityList = {
			{"???", "???", "???"}
	} ;
	
	static public Map<String,Capability> getCapabilityList ( String agentName )
	{
		//List<Capability> capabilityList = new ArrayList<Capability>() ;
		Map<String,Capability> capabilityList = new HashMap<String,Capability>() ;
		
		for (int i = 0; i < agentCapabilityList.length; i++)
		{
			if ( agentCapabilityList[i][0].equalsIgnoreCase(agentName) )
			{
				capabilityList.put(agentName, new Capability ( agentCapabilityList[i] ) ) ;
			}
				
		}
		
		return capabilityList ;
	}
	
	
	static public String[] getAgentNameList ()
	{
		return agentList ;
	}
		
	

}
