package cs601.project4;


/**
 * Hold information corresponding to a row in the ticket table.
 * @author Kei Fukutani
 */
public class MyTicket {
	
	private int ticketId;
	private String eventName;
	private String owner;
	
	
	public int getTicketId() {
		return ticketId;
	}
	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	
	
	
	

}
