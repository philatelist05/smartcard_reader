package at.ac.tuwien.mnsa.midlet;

import javax.microedition.contactless.TargetListener;
import javax.microedition.contactless.TargetProperties;
import javax.microedition.contactless.TargetType;

public class CardConnector implements TargetListener {

    private TargetProperties targetProperty;

    public void targetDetected(TargetProperties[] targetProperties) {
		for (int i = 0; i < targetProperties.length; i++) {
			if (targetProperties[i].hasTargetType(TargetType.ISO14443_CARD)
					&& targetProperties[i].getConnectionNames().length > 0) {
                targetProperty = targetProperties[i];
            }
		}
	}

	public TargetProperties getLatestTargetProperties() {
		return targetProperty;
	}

	public boolean isCardPresent() {
        return targetProperty != null;
    }
}
