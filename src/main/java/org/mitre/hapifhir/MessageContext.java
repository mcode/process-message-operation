package org.mitre.hapifhir;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.MessageHeader;
import javax.servlet.http.HttpServletRequest;

public class MessageContext {
  public Bundle bundle;
  public MessageHeader messageHeader;
  public HttpServletRequest request;
}
