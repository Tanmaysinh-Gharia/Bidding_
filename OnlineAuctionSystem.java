import java.util.Scanner;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.String;

public class OnlineAuctionSystem {
    static int bid_ID = 0;
    static int auct_ID = 0;

    static HashMap<Integer, List<String>> auctioMap = new HashMap<Integer, List<String>>();
    // auct_id--------auctname, auction_status, lot, min_bid, min_bid_incrm, bid_id,
    // curr_bid
    static HashMap<Integer, List<String>> biddMap = new HashMap<Integer, List<String>>();

    // class Auction {
    // boolean openAuction() {

    // }

    // boolean closeAuction() {

    // }

    // String Winning_Bids()
    // {

    // }

    void Create_Auction(String Auc_name, int lot, int min_bid, int min_bid_Incrm) {
        if (auctioMap.containsKey(auct_ID)) {
            System.out.println("\nFor This name auction is already there try another one..");
            return;
        }
        auctioMap.put(auct_ID, new ArrayList<String>());
        auctioMap.get(auct_ID)
                .add(Auc_name + "-" + 0 + "-" + lot + "-" + min_bid + "-" + min_bid_Incrm + "-" + 0 + "-" + 0);
        System.out.println(auctioMap);
        auct_ID++;
    }
    // }

    // class Bidder extends Auction {
    // // int get_Bidder_ID()
    // // {

    // // }

    // }
    void create_Bidder(String Bidr_name) {
        if (Bidr_name == "") {
            System.out.println("\nPlease Enter Valid Name..");
            return;
        }
        biddMap.put(bid_ID, new ArrayList<String>());
        biddMap.get(bid_ID).add(Bidr_name + "-" + 0 + "-" + 0);
        System.out.println(biddMap);
        bid_ID++;
    }

    // String auction_status()
    // {

    // }

    void load_bids(String file_name) {
        try {
            Path filename = Path.of(file_name);
            String str = Files.readString(filename);
            if (str == "") {
                System.out.println("nothing in File");
                return;
                
            }
            String[] str_arr = str.split("-");
            
            
        } catch (Exception e) {
            System.out.println("Invalid Input...");
        }

    }

    // int place_bid(int lot_no,int bidder_id,int bid)
    // {

    // }

    // String fees_Owned()
    // {

    // }

    // String winning_bids()
    // {

    // }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        OnlineAuctionSystem oas = new OnlineAuctionSystem();
        // OnlineAuctionSystem ptr;
        boolean loop_indc = true;
        int loop_direct;
        while (loop_indc == true) {

            System.out.print(
                    "\n1..Creates Auction and assign lots\n2..To Add bidder\n3..Open an Auction for bidding\n4..Retrive the currunt bid on a lot\n5..Place a bid on a lot\n6..close an auction\n7..Report on the winning bids for all lots in an auction\n\nEnter Choice::->");
            loop_direct = in.nextInt();
            switch (loop_direct) {
                case 1:
                    System.out.println("\n\nAUCTION CREATED...");
                    // boolean auct_indc = true;
                    int lot = 0, min_bid_Incrm = 0, min_bid = 0;
                    System.out.println("Auction name will be ID For the Auction");
                    System.out.println("Enter Auction Name below :-");
                    in.nextLine();
                    String auct_name = in.nextLine();
                    System.out.println("Your AUCTION ID : " + ((auct_ID == 0) ? (0) : (auct_ID - 1)));
                    while (true) {
                        System.out.println("Enter '-1' in lots to End Lots Insertion..");
                        System.out.print("\nEnter Lots : ");
                        lot = in.nextInt();
                        if (lot == -1) {
                            System.out.println("Ended LOT Insertion..");
                            break;
                        }
                        System.out.print("\nEnter Bid Starting Amount : ");
                        min_bid = in.nextInt();
                        System.out.print("\nMinimum Bid Increment : ");
                        min_bid_Incrm = in.nextInt();
                        System.out.println(" Another lot.....");
                        if ((lot != 0) && (min_bid >= 0) && (min_bid_Incrm >= 0)) {
                            oas.Create_Auction(auct_name, lot, min_bid, min_bid_Incrm);
                        } else
                            System.out.println("\nEnter Valid Input...");
                    }

                    break;

                case 2:
                    System.out.print("\n\nEnter Bidder's Name below : ");
                    in.nextLine();
                    String Bidr_name = in.nextLine();
                    int temp = bid_ID;
                    oas.create_Bidder(Bidr_name);
                    if (temp != bid_ID) {
                        System.out.println("\n\nBidder Added....");
                        System.out.println("\n\nBidder details..." + Bidr_name + "\nBidder's Unique ID : " + (temp));
                    }
                    break;

                case 3:
                    System.out.print("Enter Auction ID to Start: ");
                    in.nextLine();
                    String au_ID = in.nextLine();
                    if (auctioMap.containsKey(au_ID) == false) {
                        System.out.println("Auction doesn't Exist...");
                        break;
                    }
                    try {

                        System.out.println("Enter File Path to dispatch data from it...:");
                        in.nextLine();
                        String fp = in.nextLine();

                        oas.load_bids(fp);

                    } catch (Exception e) {
                        System.out.println("Exception : Enter Correct PATH....");
                        break;
                    }

                case 4:

                default:
                    break;
            }

        }

    }

}

// class Auction extends OnlineAuctionSystem{
// boolean openAuction()
// {

// }

// boolean closeAuction()
// {

// }

// String Winning_Bids()
// {

// }

// Auction Create_Auction(String Auc_name, int first_lot_no,int last_lot_no,int
// min_bid_Incrm)
// {

// }
// }
