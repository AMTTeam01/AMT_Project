package ch.heigvd.amt.mvcProject.application;

public class VoteUtils {

    // Vote values
    public static final int UPVOTE      =  1;
    public static final int NOVOTE      =  0;
    public static final int DOWNVOTE    = -1;
    public static final int NONEXISTANT = -2;

    /**
     * Get the vote value when voting on a quesiton
     * @param startVoteValue : start vote value of the user (if he already voted on the question)
     * @param voteValue : vote value of the current vote
     * @return the new vote value
     */
    public static int getNewVoteValue(int startVoteValue, int voteValue) {
        int result = 0;

        if(voteValue == UPVOTE) {
            switch(startVoteValue) {
                case NOVOTE:
                case DOWNVOTE:
                    result = UPVOTE;
                    break;
                case UPVOTE:
                    result = 0;
                    break;
            }
        } else if (voteValue == DOWNVOTE) {
            switch(startVoteValue) {
                case NOVOTE:
                case UPVOTE:
                    result = DOWNVOTE;
                    break;
                case DOWNVOTE:
                    result = 0;
                    break;
            }
        }

        return result;
    }

}
