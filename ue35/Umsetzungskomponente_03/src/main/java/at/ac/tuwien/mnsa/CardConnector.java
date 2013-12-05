package at.ac.tuwien.mnsa;

import javax.microedition.contactless.TargetListener;
import javax.microedition.contactless.TargetProperties;
import javax.microedition.contactless.TargetType;
import java.util.LinkedList;

public class CardConnector implements TargetListener {

	private final LinkedList list;

	public CardConnector() {
		list = new LinkedList();
	}

	public void targetDetected(TargetProperties[] targetProperties) {
		for (int i = 0; i < targetProperties.length; i++) {
			if (targetProperties[i].hasTargetType(TargetType.ISO14443_CARD)
					&& targetProperties[i].getConnectionNames().length > 0) {
				list.addFirst(targetProperties[i]);
			}
		}
	}

	public TargetProperties getLatestTargetProperties() {
		return (TargetProperties) list.getFirst();
	}

	public boolean isCardPresent() {
		return list.size() > 0;
	}
}
