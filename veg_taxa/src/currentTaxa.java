public class currentTaxa
{
	public currentTaxa()
	{

		outStandardUsage = null;
		outStandardUsageNum = 0;
		outNonStandardUsage = null;
		outNonStandardUsageNum = 0;

		outStandardCorrelation = null;
		outStandardCorrelationNum = 0;
		outNonStandardCorrelation = null;
		outNonStandardCorrelationNum = 0;

	}

	public void getCurrentTaxon(String input_s, int input_party)
	{

		try
		{
			String firstName = input_s;

			/*get all the maching names*/
			nameQuery m = new nameQuery();
			m.getName(firstName);
			String nameArray[] = new String[10];
			int nameId[] = m.outNameId;
			int nameNum = m.outNameNum;
			nameArray = m.outName;

			for (int ii = 0; ii < nameNum; ii++)
			{

				/*get the circum_id from the  uses of that name*/
				usageQuery m1 = new usageQuery();
				m1.getNameId(nameId[ii]);
				int nameUsageId[] = m1.outNameUsageId;
				int circumId[] = m1.outCircumId;
				int usagePartyId[] = m1.outPartyId;
				int nameUsageIdNum = m1.outNameUsageNum;
				String nameAcceptence[] = m1.outAcceptence;

				System.out.println("NameId: " + nameId[ii] + " name: " + nameArray[ii]);

				for (int i3 = 0; i3 < nameUsageIdNum; i3++)
				{
					/*detrmine if the usage is by the target party*/
					if (usagePartyId[i3] == input_party)
					{

						System.out.println(
							"  nameUsageId: "
								+ nameUsageId[i3]
								+ " circumId: "
								+ circumId[i3]
								+ " usagePartyId: "
								+ usagePartyId[i3]
								+ " acceptence: "
								+ nameAcceptence[i3]);

						/*get the status from the circumscription used for that name*/
						statusQuery m2 = new statusQuery();
						m2.getCircumId(circumId[i3]);
						int statusCircumId[] = m2.outStatusCircumId;
						int statusNum = m2.outStatusNum;
						int statusPartyId[] = m2.outPartyId;
						String statusStartDate[] = m2.outStartDate;
						String statusStopDate[] = m2.outStopDate;
						String status[] = m2.outStatus;

						for (int i4 = 0; i4 < statusNum; i4++)
						{

							/*continue with the usages of the target party*/

							if (statusPartyId[i4] == input_party)
							{

								System.out.println(
									"    statusCircumId: "
										+ statusCircumId[i4]
										+ " staus: "
										+ status[i4]
										+ " partyID: "
										+ statusPartyId[i4]
										+ " "
										+ statusStartDate[i4].substring(0, 4)
										+ "-"
										+ statusStopDate[i4].substring(0, 4));

								/*check to see if usage is non standard*/
								if (nameAcceptence[i3].trim().equals("A"))
								{ //if 3
									System.out.println("non standard usage");
									usageQuery m3 = new usageQuery();
									m3.getCircumId(circumId[i3]);
									int acceptedNameUsageId[] = m3.outNameUsageId;
									int acceptedCircumId[] = m3.outCircumId;
									int acceptedPartyId[] = m3.outPartyId;
									int acceptedUsageIdNum = m3.outNameNum;
									int acceptedNameId[] = m3.outNameId;
									String acceptedAcceptence[] = m3.outAcceptence;
									String acceptedStartDate[] = m3.outStartDate;
									String acceptedStopDate[] = m3.outStopDate;

									for (int i5 = 0; i5 < acceptedUsageIdNum; i5++)
									{ //for 4

										if (acceptedAcceptence[i5].trim().equals("S")
											&& (acceptedPartyId[i5] == input_party))
										{ //if 4 
											System.out.println(
												"NameId: "
													+ acceptedNameId[i5]
													+ " party id: "
													+ acceptedPartyId[i5]
													+ " acceptence: "
													+ acceptedAcceptence[i5]
													+ " acceptedCircumId:"
													+ acceptedCircumId[i5]
													+ " startDate: "
													+ acceptedStartDate[i5]
													+ " acceptedStopDate: "
													+ acceptedStopDate[i5]);

											nameQuery m4 = new nameQuery();
											m4.getNameId(acceptedNameId[i5]);

											String acceptedNameArray[] = m4.outName;
											int acceptedNameNum = m4.outNameNum;

											for (int i6 = 0; i6 < acceptedNameNum; i6++)
											{ //for 5
												System.out.println(
													"Accepted taxonName: " + acceptedNameArray[i6]);
												/*write the output to the public variable*/
												outNonStandardUsage[outNonStandardUsageNum] =
													(nameArray[ii]
														+ "|"
														+ acceptedNameArray[i6]
														+ "|"
														+ acceptedStartDate[i5].substring(0, 4)
														+ "|"
														+ acceptedStopDate[i5].substring(0, 4));
												outNonStandardUsageNum++;

											} //end for 5

										} //end if 4

									} //end for 4
									System.out.println(" ");
								} //end if3

								/*else the usage is standard and the input string is equal to the current taxon*/

								else
								{

									/*make sure to only point the output to the standard status*/
									if (status[i4].trim().equals("S"))
									{
										outStandardUsage[outStandardUsageNum] =
											(nameArray[ii]
												+ "|"
												+ nameArray[ii]
												+ "|"
												+ statusStartDate[i4].substring(0, 4)
												+ "|"
												+ statusStopDate[i4].substring(0, 4));
										outStandardUsageNum++;
									}
								} //end else

							} //end if 		
						} //end innerFor2
					} //end if
				} //end innerFor

			} //end for

		} //end try
		catch (Exception e)
		{
			System.out.println(" failed at: currentTaxa  " + e.getMessage());
		}

	}

	public void getTaxaCorrelation(String input_s, int input_party)
	{
		try
		{
			String firstName = input_s;

			/*get all the maching names*/
			nameQuery m = new nameQuery();
			m.getName(firstName);
			String nameArray[] = new String[10];
			int nameId[] = m.outNameId;
			int nameNum = m.outNameNum;
			nameArray = m.outName;

			for (int ii = 0; ii < nameNum; ii++)
			{

				/*get the circum_id from the  uses of that name*/
				usageQuery m1 = new usageQuery();
				m1.getNameId(nameId[ii]);
				int nameUsageId[] = m1.outNameUsageId;
				int circumId[] = m1.outCircumId;
				int usagePartyId[] = m1.outPartyId;
				int nameUsageIdNum = m1.outNameUsageNum;
				String nameAcceptence[] = m1.outAcceptence;

				System.out.println("NameId: " + nameId[ii] + " name: " + nameArray[ii]);

				for (int i3 = 0; i3 < nameUsageIdNum; i3++)
				{
					/*detrmine if the usage is by the target party*/
					if (usagePartyId[i3] == input_party)
					{

						System.out.println(
							"  nameUsageId: "
								+ nameUsageId[i3]
								+ " circumId: "
								+ circumId[i3]
								+ " usagePartyId: "
								+ usagePartyId[i3]
								+ " acceptence: "
								+ nameAcceptence[i3]);

						/*get the status from the circumscription used for that name*/
						statusQuery m2 = new statusQuery();
						m2.getCircumId(circumId[i3]);
						int statusCircumId[] = m2.outStatusCircumId;
						int statusNum = m2.outStatusNum;
						int statusPartyId[] = m2.outPartyId;
						String statusStartDate[] = m2.outStartDate;
						String statusStopDate[] = m2.outStopDate;
						String status[] = m2.outStatus;

						for (int i4 = 0; i4 < statusNum; i4++)
						{

							/*continue with the usages of the target party*/

							if (statusPartyId[i4] == input_party)
							{

								System.out.println(
									"    statusCircumId: "
										+ statusCircumId[i4]
										+ " staus: "
										+ status[i4]
										+ " partyID: "
										+ statusPartyId[i4]
										+ " "
										+ statusStartDate[i4].substring(0, 4)
										+ "-"
										+ statusStopDate[i4].substring(0, 4));

								/*if the usage is a standard then write to the public*/
								if (status[i4].equals("S"))
								{
									outStandardCorrelation[outStandardCorrelationNum] =
										(nameAcceptence[ii]
											+ "|"
											+ nameArray[ii]
											+ "|"
											+ statusCircumId[i4]
											+ "|"
											+ status[i4]
											+ "|"
											+ statusPartyId[i4]
											+ "|"
											+ statusStartDate[i4].substring(0, 4)
											+ "|"
											+ statusStopDate[i4].substring(0, 4));
									outStandardCorrelationNum++;
								}

								/*get the correlation if this is a nonstandard usage*/

								if (status[i4].equals("N"))
								{
									correlationQuery m3 = new correlationQuery();
									m3.getCorrelationId(statusCircumId[i4]);
									int correlationId[] = m3.outCorrelationId;
									int correlationNum = m3.outCorrelationNum;
									String congruence[] = m3.outCongruence;
									int statusCircum2[] = m3.outStatusCircum2;
									for (int i5 = 0; i5 < correlationNum; i5++)
									{
										System.out.println(
											"       correlationId: "
												+ correlationId[i5]
												+ " congruence: "
												+ congruence[i5]
												+ " statusCircum2: "
												+ statusCircum2[i5]);

										/*get the circum number of the correlated value*/

										statusQuery m4 = new statusQuery();
										m4.getStatusCircumId(statusCircum2[i5]);
										int correlationCircumId[] = m4.outCircumId;
										String correlationStatus[] = m4.outStatus;
										int correlationCircumNum = m4.outStatusNum;
										for (int i6 = 0; i6 < correlationCircumNum; i6++)
										{

											System.out.println(
												"        correlationCircumId: "
													+ correlationCircumId[i6]
													+ " correlationCircumStatus: "
													+ correlationStatus[i6]);

											/*get the name_id associated with this circum id*/

											usageQuery m5 = new usageQuery();
											m5.getCircumId(correlationCircumId[i6]);
											int correlationNameId[] = m5.outNameId;
											int correlationNameNum = m5.outNameNum;
											int correlationPartyId[] = m5.outPartyId;
											String correlationStartDate[] = m5.outStartDate;
											String correlationStopDate[] = m5.outStopDate;
											String correlationAcceptence[] = m5.outAcceptence;
											for (int i7 = 0; i7 < correlationNameNum; i7++)
											{

												System.out.println(
													"           NameId associated with correlationCircum: "
														+ correlationNameId[i7]
														+ " usageParty: "
														+ correlationPartyId[i7]
														+ " startDate: "
														+ correlationStartDate[i7].substring(0, 4)
														+ "stoprDate: "
														+ correlationStopDate[i7].substring(0, 4));

												/*get the name associated with the name_id*/
												nameQuery m6 = new nameQuery();
												m6.getNameId(correlationNameId[i7]);
												String currentName[] = m6.outName;
												int currentNameNum = m6.outNameNum;

												for (int i8 = 0; i8 < currentNameNum; i8++)
												{

													System.out.println(
														"              Name associated: "
															+ currentName[i8]);
													System.out.println("            ");
													System.out.println("            ");

													/*write to the public variables*/
													if (correlationPartyId[i7] == input_party)
													{

														outNonStandardCorrelation[outNonStandardCorrelationNum] =
															(nameAcceptence[i3]
																+ "|"
																+ nameArray[ii]
																+ "|"
																+ statusCircumId[i4]
																+ "|"
																+ correlationId[i5]
																+ "|"
																+ congruence[i5]
																+ "|"
																+ statusCircum2[i5]
																+ "|"
																+ correlationCircumId[i6]
																+ "|"
																+ correlationStatus[i6]
																+ "|"
																+ correlationNameId[i7]
																+ "|"
																+ correlationPartyId[i7]
																+ "|"
																+ correlationStartDate[i7].substring(0, 4)
																+ "|"
																+ correlationStopDate[i7].substring(0, 4)
																+ "|"
																+ currentName[i8]
																+ "|"
																+ correlationAcceptence[i7]);
														outNonStandardCorrelationNum++;
													}

												}
											} // end innerFor5

										} //end innerFor4

									} // end if
								} //end innerFor3

							} //end if
						} //end if
					} //end innerFor2

				} //end innerFor

			} //end for

		}
		catch (Exception e)
		{
			System.out.println(" failed at: currentTaxa  " + e.getMessage());
		}

	}

	public String outStandardUsage[] = new String[1000];
	public String outNonStandardUsage[] = new String[1000];
	public int outStandardUsageNum;
	public int outNonStandardUsageNum;

	/*below are the strings containing the information for the correlation lookup component of the servlet*/
	public String outStandardCorrelation[] = new String[1000];
	public String outNonStandardCorrelation[] = new String[1000];
	public int outStandardCorrelationNum;
	public int outNonStandardCorrelationNum;

	//public String output_s;
	//public String outName[]=new String[1000];
	//public int outNameId[]=new int[1000];
	//public int outNameNum;
	//public int outCircumStatusNum;
	//public String outCircumStatus[]=new String[1000];
	//public String outCorrelativeCircum[]=new String[1000];
	//int outCorrelativeCircumNum;	

}
