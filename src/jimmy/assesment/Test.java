package jimmy.assesment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {

	public static void main(String args[]) {
		List<Message> shouts = new ArrayList<>();
		shouts.add(new Message(0, "", "", 3,"", 4));
		shouts.add(new Message(0, "", "", 3,"", 5));
		shouts.add(new Message(0, "", "", 3,"", 6));
		Collections.sort(shouts);
		System.out.println(shouts);
		
	}
}