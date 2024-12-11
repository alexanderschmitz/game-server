package de.tu_berlin.pjki_server.server_interface.packets;

public class AbstractPacket {
	
	
	public static enum Type {
		INVALID(-1), LOGIN(0), GETGAMES(1), CREATEGAME(2), JOIN(3), MOVE(4), DATABASE(5);

		private int typeID;
		
		Type(int typeID) {
			this.typeID = typeID;
		}

		public int getTypeID() {
			return typeID;
		}		
	}
	
	private Type type;
	
	public AbstractPacket(Type type) {
		this.type = type;
	}
	
	public static Type getPacketType(String packetID) {
		try {
			return getPacketType(Long.parseLong(packetID));
		} catch (Exception e) {
			return Type.INVALID;
		}
	}
	
	public static Type getPacketType(long packetID) {
		for (Type type: Type.values()) {
			if (type.getTypeID() == packetID) {
				return type;
			}
		}
		return Type.INVALID;
	}
	

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	
	
}
