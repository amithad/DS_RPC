package CMS.FT;

import CMS.Util.Neighbour;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by amitha on 1/24/17.
 */
public class FTMsgDecoder {

    public static String decodeFTMsg(FTMan ftMan, String msg, String senderIP) throws IOException, InterruptedException {
        StringTokenizer st = new StringTokenizer(msg, " ");
        String length = st.nextToken();
        String responseType = st.nextToken();

        BSMsgType t = BSMsgType.valueOf(responseType);

        switch (t) {
            case ERROR: {
                ftMan.echo("Invalid request error!");
                break;
            }
            case REGOK: {
                int nodeCount = Integer.parseInt(st.nextToken());
                switch (nodeCount) {
                    case 0: {
                        ftMan.echo("Registration successful. No nodes in the system.");
                        break;
                    }

                    case 1: {
                        Neighbour n1 = new Neighbour(st.nextToken(), Integer.parseInt(st.nextToken())); //add neighbour
                        ftMan.addNeighbour(n1);
                        ftMan.echo("Registration successful. One neighbour added.");
                        break;
                    }

                    case 2: {
                        Neighbour n1 = new Neighbour(st.nextToken(), Integer.parseInt(st.nextToken())); //add neighbours
                        ftMan.addNeighbour(n1);
                        Neighbour n2 = new Neighbour(st.nextToken(), Integer.parseInt(st.nextToken()));
                        ftMan.addNeighbour(n2);
                        ftMan.echo("Registration successful. Two neighbours added.");
                        break;
                    }

                    case 9999: {
                        ftMan.echo("Message Syntax Error. Socket disconnecting...");
                        ftMan.disconnectBS();
                        break;
                    }

                    case 9998: {
                        ftMan.echo("Already registered. Unregistering and reattempting...");
                        ftMan.sendUNREG();
                        ftMan.sendREG();
                        break;
                    }

                    case 9997: {
                        ftMan.echo("Failed. Registered to another user. Socket disconnecting...");
                        //handle by changing username once and if happens again, reinitialize.
                        break;
                    }

                    case 9996: {
                        ftMan.echo("Bootstrap Server full. Socket disconnecting...");
                        break;
                    }
                    default:
                        ftMan.echo("Unsupported REGOK responseFlag: " + t);
                        break;
                }
                break;

            }
            case UNROK: {
                int responseFlag = Integer.parseInt(st.nextToken());
                if (responseFlag == 0)
                    ftMan.echo("UNREG successful!");
                else if (responseFlag == 9999) {
                    ftMan.echo("Error while unregistering!");
                    ftMan.sendUNREG();
                } else {
                    ftMan.echo("Unsupported UNROK responseFlag: " + responseFlag);
                }
                break;
            }

            case JOIN: {
                Neighbour n1 = new Neighbour(st.nextToken(), Integer.parseInt(st.nextToken()));
                ftMan.addNeighbour(n1);
                int senderPort = ftMan.getNeighbourPort(senderIP);
                if (senderPort != 0) {
                    ftMan.sendJOINOK(senderIP, senderPort, 0);
                }
                break;
            }
            case JOINOK: {
                int responseFlag = Integer.parseInt(st.nextToken());
                if (responseFlag == 0) {
                    ftMan.echo("Join request to "+senderIP+" was successful.");
                } else if (responseFlag == 9999) {
                    ftMan.echo("Error while adding new node to routing table.");
                } else {
                    ftMan.echo("Unsupported JOINOK responseFlag: " + responseFlag);
                }
                break;
            }

            case LEAVE: {
                ftMan.removeNeighbour(st.nextToken());
                break;
            }
            default: {
                ftMan.readDSMsg(msg,senderIP);
                break;
            }
        }
        return null;
    }

}
