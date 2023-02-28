import java.util.Scanner;

import javax.swing.RowFilter.Entry;

// import Bidder_list.inner_lot_map;

// import Bidder_list.inner_lot_map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.String;

class Bidder_list {
    int[] ptr;
    static int Bidder_ID = 1;
    HashMap<Integer, String> map_bidr_ID_name = new HashMap<Integer, String>();
    HashMap<Integer, int[]> map_bidr_vals = new HashMap<Integer, int[]>();
    int[] ptr_arr;

    int Add_Bidder(String name) {
        if (name != "") {
            map_bidr_ID_name.put(Bidder_ID, name);
            map_bidr_vals.put(Bidder_ID, new int[2]);
            return Bidder_ID++;
        } else
            return 0;
    }

    class Auction {
        // boolean is_auct_open = false;
        // int min_bid_Incrm = 1,first_lot_no,last_lot_no,min_bid;
        // inner_lot_map lots = new inner_lot_map(first_lot_no, last_lot_no);
        HashMap<String, inner_lot_map> map_auction = new HashMap<String, inner_lot_map>();

        boolean Add_Auction(String auct_name, int first_lot_no, int last_lot_no, int min_bid_Incrm) {
            try {
                map_auction.put(auct_name, new inner_lot_map(first_lot_no, last_lot_no, min_bid_Incrm));
                return true;

            } catch (Exception e) {
                return false;
            }
        }

        String feesowned() {
            try {
                StringBuilder fees_str = new StringBuilder();
                int[] arr_bidr_ptr;
                int bid_ID, bid_total_amt = 0;
                int count = 0;
                for (inner_lot_map ptr_inr_lot_map : map_auction.values()) {
                    System.out.println(count++);
                    if (ptr_inr_lot_map.is_auction_open) {
                        continue;
                    }
                    for (int[] arr_ptr : ptr_inr_lot_map.lot_map.values()) {
                        if (arr_ptr[2] == 0)
                            continue;
                        bid_ID = arr_ptr[2];
                        bid_total_amt = arr_ptr[0];
                        arr_bidr_ptr = map_bidr_vals.get(bid_ID);
                        arr_bidr_ptr[0]++;
                        arr_bidr_ptr[1] = arr_bidr_ptr[1] + bid_total_amt;
                    }
                }
                for (Map.Entry<Integer, int[]> every_bidr : map_bidr_vals.entrySet()) {
                    bid_ID = every_bidr.getKey();
                    arr_bidr_ptr = every_bidr.getValue();
                    fees_str = fees_str.append(
                            (map_bidr_ID_name.get(bid_ID) + "\t" + arr_bidr_ptr[0] + "\t" + arr_bidr_ptr[1] + "\n"));

                }
                return (fees_str.toString());

            } catch (Exception e) {
                System.out.println(e);
                return "inerror";
            }
        }

    }

    class inner_lot_map extends Auction {
        boolean is_auction_open;
        int min_bid_Incrm;
        HashMap<Integer, int[]> lot_map = new HashMap<Integer, int[]>();
        inner_lot_map ptr_inr_lot_map;
        int[] arr_ptr;

        inner_lot_map(int first_lot_no, int last_lot_no, int min_bid_Incrm) {
            is_auction_open = false;
            for (int i = first_lot_no; i <= last_lot_no; i++) {
                lot_map.put(i, new int[3]);
                ptr = lot_map.get(i);
                // ptr[0] = min_bid_Incrm;
                this.min_bid_Incrm = min_bid_Incrm;
                // MAP_____ Lot_no : [ prev_valid_bid , max_bid , bid_ID ]
            }
            System.out.println("Auction data..");
            for (int i : lot_map.keySet()) {
                ptr = lot_map.get(i);
                System.out.println(" lot no. : " + i + " --> " + ptr[0] + " " + ptr[1] + " " + ptr[2]);
            }
        }

        int check_bid(int lot_n, int bid_amt, int b_id) {
            // if (is_auct_open == false)
            // System.out.println("This Auction is closed...");
            if (b_id <= 0 || lot_n <= 0 || bid_amt <= 0) {
                System.out.println(
                        "Given data is not correct : bidder_ID , Bidder_amt, Lot_number can't be zero or negative");
            }
            if (lot_map.containsKey(lot_n)) {
                ptr = lot_map.get(lot_n);
                int val_bid = ptr[0] + min_bid_Incrm;

                if (bid_amt < val_bid)
                    return 0;

                else if ((bid_amt >= val_bid) && (val_bid > ptr[1]))
                    return 1;
                else if ((bid_amt >= val_bid) && (val_bid <= ptr[1]))
                    return 2;
                else
                    return -1;

            } else {
                System.out.println("Invalid Lot Number.....i.e." + lot_n);
                System.out.println("Available lots are given below ");
                for (int i : lot_map.keySet())
                    System.out.println("Lot Number : " + i);
                return -1;
            }
        }

        void place_bid(int lot_n, int bid_amt, int b_id, int bid_type) {
            int valid_bid = ptr[0] + min_bid_Incrm;
            ptr = lot_map.get(lot_n);

            switch (bid_type) {

                case 0:
                    System.out.println("Bid amount is too low to get lot..now bid amount is : " + ptr[0]);
                    break;

                case 1:
                    ptr[0] = valid_bid;
                    ptr[1] = bid_amt;
                    ptr[2] = b_id;
                    break;

                case 2:
                    ptr[0] = valid_bid;
                    break;

                case -1:
                    System.out.println("Your Bid is Invalid...\nBid is not placed....");
                    break;
                default:
                    System.out.println("Invalid");
                    break;
            }
        }

        static String reader(String file_path) {
            try {
                String str = new String(Files.readAllBytes(Paths.get(file_path)));
                return str;
            } catch (Exception e) {
                System.out.println("File doesn't Exist at current Address : " + file_path);
                System.out.println("\nCan't Resolve this address...");
                return "";
            }
        }

        static int str_to_int(String str) {
            int res = Integer.parseInt(str);
            return res;
        }

        void load_bids(String data_o_file) {
            if (is_auction_open) {
                int line_count = 0;
                try {
                    data_o_file = reader(data_o_file);
                    int bid_ID, lot_no, bid_amt, bid_type;
                    // System.out.println("arrived");
                    String[] word, lines = data_o_file.split("\n");
                    // System.out.println("forwarded");
                    for (String bids : lines) {
                        line_count++;
                        word = bids.split("\t");
                        if (word[0] == null || bids.equals("")) {
                            break;
                        }
                        System.out.println(word[0] + word[1] + word[2] + "len :" + word.length);
                        if (word.length > 3) {
                            throw new ArrayIndexOutOfBoundsException(
                                    "At any line " + line_count + "More values then required");
                        }
                        // System.out.println("HELLO before parse");
                        bid_ID = str_to_int(word[0].strip());
                        // System.out.println("HELLO before parse");
                        lot_no = str_to_int(word[1].strip());
                        // System.out.println("HELLO before parse");
                        bid_amt = str_to_int(word[2].strip());
                        // System.out.println("HELLO before parse");
                        try {
                            // System.out.println("hello in if");
                            if (map_bidr_ID_name.containsKey(bid_ID)) {
                                bid_type = check_bid(lot_no, bid_amt, bid_ID);
                                // System.out.println("Bid_type : " + bid_type);
                                place_bid(lot_no, bid_amt, bid_ID, bid_type);
                            } else {
                                System.out.println(
                                        "Given Bidder doesn't exist in system please enter it i.e. Bid_ID" + bid_ID);
                                throw new Exception("Bidder Doesn't exists");
                            }

                        } catch (Exception e) {
                            System.out.println("Something wrong at line " + line_count + "moving forwand");
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("Given data formate isn't correct completely.. at line " + line_count);
                }
            } else
                System.out.println("Auction is not opened.. please open first to load bids..");

        }

        void setOpen() {
            is_auction_open = true;
        }

        void setClose() {
            is_auction_open = false;
        }

        String winning_Bid() {
            try {
                String win_list = "";
                for (int i : lot_map.keySet()) {
                    arr_ptr = lot_map.get(i);
                    win_list = win_list + i + "\t" + arr_ptr[1] + "\t" + arr_ptr[2] + "\t" + arr_ptr[0] + "\n";
                }
                return (win_list.toString());

            } catch (Exception e) {
                System.out.println("Some problem in internal system..try again");
                return null;
            }
        }

    }

}

public class OnlineAuctionSystem {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean loop_indc = true;

        Bidder_list bdr_list = new Bidder_list();
        Bidder_list.Auction allAuctions = bdr_list.new Auction();
        String auct_name,
                Bidr_name,
                file_path,
                report;

        int temp,
                loop_direct = 1,
                min_bid_Incrm = 0,
                first_lot_no = 0,
                last_lot_no = 0;

        boolean lot_indc = true;
        Bidder_list.inner_lot_map ptr_to_inr_lot_mapclass;
        while (loop_indc == true) {
            try {
                System.out.print(
                        "\n1..Creates Auction and assign lots\n2..To Add bidder\n3..Open an Auction for bidding\n4..Close an auction\n5..Load bids in auction\n6..Auction Status\n7..Report on the winning bids for all lots in an auction\n8..Lots owned by bidders\n9..Get Bidder ID\nEnter Choice::->");
                loop_direct = in.nextInt();

            } catch (Exception e) {
                System.out.println("Enter valid input among numbers...");
                continue;
            }
            lot_indc = true;
            for (int id_i : bdr_list.map_bidr_ID_name.keySet()) {
                System.out.println("Bidder ID : " + id_i + " -> Name : " + bdr_list.map_bidr_ID_name.get(id_i));
            }
            switch (loop_direct) {

                case 1:
                    System.out.println("\n\nAUCTION CREATION...");
                    // boolean auct_indc = true;
                    System.out.println("Auction name will be ID For the Auction");
                    System.out.print("Enter Auction Name below : ");
                    in.nextLine();
                    auct_name = in.nextLine();
                    if (auct_name == null || auct_name == "") {
                        System.out.println("\n\nEnter The auction name please\n\n");
                        continue;

                    }

                    System.out.print("Enter Minimum Increment in bid : ");
                    min_bid_Incrm = in.nextInt();
                    if (min_bid_Incrm <= 0) {
                        System.out.println("\nMinimum bid increment can't be negative or zero....");
                        continue;
                    }
                    lot_indc = true;
                    while (lot_indc) {
                        System.out.print("Enter Starting(first) Lot number : ");
                        first_lot_no = in.nextInt();
                        System.out.print("Enter Ending(last)  Lot number : ");
                        last_lot_no = in.nextInt();
                        if (first_lot_no <= last_lot_no) {
                            lot_indc = false;
                            break;
                        }
                        System.out.println(
                                "Note-->Enter valid range starting lot should less or equal to ending lot number..");
                    }

                    if (allAuctions.Add_Auction(auct_name, first_lot_no, last_lot_no, min_bid_Incrm))
                        System.err.println("Auction is created successfully..");
                    else
                        System.out.println("There is some problem in creation please retry...");
                    break;

                case 2:

                    System.out.print("\n\nEnter Bidder's Name  : ");
                    in.nextLine();
                    Bidr_name = in.nextLine();
                    if (Bidr_name == null || Bidr_name == "") {
                        System.out.println("It con't be empty or null");
                        continue;
                    }
                    temp = bdr_list.Add_Bidder(Bidr_name);

                    if (temp > 0) {
                        System.out.println("\n\nBidder Added....");
                        System.out.println("\n\nBidder details..." + Bidr_name + "\nBidder's Unique ID : " + (temp));
                    } else
                        System.out.println("\nBidder was not entered..try again...");

                    break;

                case 3:
                    System.out.println("Enter Auction name to open : ");
                    in.nextLine();
                    auct_name = in.nextLine();
                    if (allAuctions.map_auction.containsKey(auct_name)) {
                        ptr_to_inr_lot_mapclass = allAuctions.map_auction.get(auct_name);
                        ptr_to_inr_lot_mapclass.setOpen();
                        System.out.println("Auction :" + auct_name + " is now open to bid");
                    } else
                        System.out.println("Given name is not present as Auction in List\nTry Again..");
                    break;

                case 4:
                    System.out.println("Enter Auction name to close(end) : ");
                    in.nextLine();
                    auct_name = in.nextLine();
                    if (allAuctions.map_auction.containsKey(auct_name)) {
                        ptr_to_inr_lot_mapclass = allAuctions.map_auction.get(auct_name);
                        ptr_to_inr_lot_mapclass.setClose();
                        System.out.println("Auction :" + auct_name + " has now ended for bid.");
                    } else
                        System.out.println("Given name is not present as Auction in List of Auction\nTry Again..");
                    break;

                case 5:
                    System.out.println("Enter Auction name in which you want to bid  : ");
                    in.nextLine();
                    auct_name = in.nextLine();
                    if (allAuctions.map_auction.containsKey(auct_name)) {
                        ptr_to_inr_lot_mapclass = allAuctions.map_auction.get(auct_name);
                        // ptr_to_inr_lot_mapclass.setOpen();
                        // System.out.println("Auction :" + auct_name + " has now ended for bid.");
                        System.out.println("Enter filepath(.txt) to load bids : ");
                        // in.nextLine();
                        file_path = in.nextLine();
                        ptr_to_inr_lot_mapclass.load_bids(file_path);
                    } else
                        System.out.println("Given name is not present as Auction in List of Auction\nTry Again..");
                    break;

                case 6:
                    System.out.println("Enter auction name to see status :");
                    break;

                case 7:
                    System.out.println("Enter auction name for which you want winning bid report : ");
                    in.nextLine();
                    auct_name = in.nextLine();
                    System.out.println("\n\n\n*******Report");
                    if (allAuctions.map_auction.containsKey(auct_name))
                        report = allAuctions.map_auction.get(auct_name).winning_Bid();
                    else
                        report = "Auction doesn't exists";
                    if (report == null) {
                        report = "HI";

                    }
                    System.out.println(report);
                    break;

                case 8:
                    System.out.println("Lots Owned by Bidders those are in system....");
                    System.out.println("Bid_Name\tLots\tAmount");
                    report = allAuctions.feesowned();
                    System.out.println(report);
                    break;

                case 9:
                    System.out.println("Enter Bidder Name : ");
                    Bidr_name = in.nextLine();
                    if (bdr_list.map_bidr_ID_name.containsValue(Bidr_name)) {
                        System.out.println("Bidder Name : " + bdr_list.map_bidr_ID_name.keySet());

                    }
                    // case 7:
                    // System.out.print("Enter Auction ID to Start: ");
                    // in.nextLine();
                    // String au_ID = in.nextLine();
                    // if (auctioMap.containsKey(au_ID) == false) {
                    // System.out.println("Auction doesn't Exist...");
                    // break;
                    // }
                    // try {

                    // System.out.println("Enter File Path to dispatch data from it...:");
                    // in.nextLine();
                    // String fp = in.nextLine();

                    // oas.load_bids(fp);

                    // } catch (Exception e) {
                    // System.out.println("Exception : Enter Correct PATH....");
                    // break;
                    // }

                    // case 4:

                default:
                    break;
            }

        }
    }

}