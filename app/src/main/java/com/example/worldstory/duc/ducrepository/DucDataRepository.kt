package com.example.worldstory.duc.ducrepository


import com.example.worldstory.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducdataclass.DucCommentDataClass
import com.example.worldstory.duc.ducutils.GUEST
import com.example.worldstory.duc.ducutils.MEMBER
import com.example.worldstory.duc.ducutils.UserLoginStateEnum
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.numDef
import com.example.worldstory.duc.ducutils.toBoolean
import com.example.worldstory.model.Chapter
import com.example.worldstory.model.Comment
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Image
import com.example.worldstory.model.Paragraph
import com.example.worldstory.model.Rate
import com.example.worldstory.model.Role
import com.example.worldstory.model.Story
import com.example.worldstory.model.User

class DucDataRepository(private var dbHelper: DatabaseHelper) {
    //story
    fun getAllStories(): List<Story> {
        var list = dbHelper.getAllStories()
        return list
    }

    // fun getStoryById(id: Int): Story? = dbHelper.getStoryById(id)
    fun addStory(story: Story): Long = dbHelper.insertStory(story)

    fun getStoriesByGenre(genreId: Int, isText: Boolean): List<Story> {
        var setOfStoryId = dbHelper.getStoriesIdbyGenreId(genreId)
        var tempStory = SampleDataStory.getexampleStory()
        var listOfStories = mutableListOf<Story>()
        setOfStoryId.forEach {
            var story = dbHelper.getStoryByStoryId(it) ?: tempStory
            if (story.isTextStory.toBoolean() == isText) {
                listOfStories.add(story)
            }

        }
        return listOfStories

    }

    //genre
    fun getAllGenres(): List<Genre> {
        var list = dbHelper.getAllGenres()
        return list
    }

    fun getGenresByStory(storyId: Int): List<Genre> {
        var setOfGenresId = dbHelper.getGenreIDbyStoryID(storyId)
        var tempGenre = SampleDataStory.getexampleGenre()
        var listOfGenres = mutableListOf<Genre>()
        setOfGenresId.forEach {
            listOfGenres.add(dbHelper.getGenreByGenresId(it) ?: tempGenre)
        }
        return listOfGenres
    }

    //chapter
    fun getAllChapter(): List<Chapter> {
        var list = dbHelper.getAllChapters()
        return list
    }

    fun getChaptersByStory(storyId: Int): List<Chapter> {
        var list = dbHelper.getChaptersByStory(storyId)
        return list
    }

    //paragraph
    fun getAllParagraph(): List<Paragraph> {
        var list = dbHelper.getAllParagraphs()
        return list
    }

    fun getParagraphsByChapter(chapterId: Int): List<Paragraph> {
        return dbHelper.getParagraphsByChapter(chapterId)
    }


    //image
    fun getImagesByChapter(chapterId: Int): List<Image> {
        return dbHelper.getImagesByChapter(chapterId)
    }

    //comment

    fun getCommentsByStory(storyId: Int): List<DucCommentDataClass> {
        var listOfComments = dbHelper.getCommentsByStory(storyId)
        var listOfDucComments = mutableListOf<DucCommentDataClass>()
        listOfComments.forEach {
            var user = dbHelper.getUserByUsersId(it.userId)
            if (user!=null){
                var ducComment = DucCommentDataClass(
                    it.commentId?: numDef,
                    it.content,
                    user.imgAvatar,
                    user.userName,
                    it.time,
                    it.storyId,
                    it.userId
                )
                listOfDucComments.add(ducComment)

            }
        }
        return listOfDucComments
    }
    fun createComment(comment: Comment){
        dbHelper.insertComment(comment)
    }
    //user

    fun addNewUserMember(username: String,password: String,email: String,nickname: String,date: String){
        var roleIdMember=getRoleIdMember()
        var newUser = User(
            null, username, password, email, SampleDataStory.getExampleAvatarUrl(), nickname, roleIdMember,
            date
        )
        dbHelper.insertUser(newUser)
    }
    fun checkAccountByUserNameWhenLogin(userName: String, password: String): UserLoginStateEnum{
        var userNameTrim= userName.trim()
        var passwordTrim=password.trim()
        var user=dbHelper.getUserByUsersName(userNameTrim)
        return if (user==null){
            UserLoginStateEnum.ACCOUNT_DOES_NOT_EXIST
        }else{
            if(user.hashedPw == passwordTrim){
                UserLoginStateEnum.CORRECT

            }else{
                UserLoginStateEnum.INCORRECT_USERNAME_OR_PASSWORD

            }


        }

    }
    fun checkAccountExist(userName: String):UserLoginStateEnum{
        var userNameTrim= userName.trim()
        var user=dbHelper.getUserByUsersName(userNameTrim)
        return if (user==null){
            UserLoginStateEnum.CORRECT
        }else{
            UserLoginStateEnum.USERNAME_ALREADY_EXISTS
        }

    }
    fun getUserByUsername(username: String): User?{
        return dbHelper.getUserByUsersName(username)
    }
    fun getUserByUserId(userId: Int): User?{
        return dbHelper.getUserByUsersId(userId)
    }
    fun getLastestUserId(): User?{
        return dbHelper.getLastestUserId()
    }
    fun createGuestUser(): User{
        var lastUser=getLastestUserId()
        var step=1
        var lasId=lastUser?.userID
        var id= lasId?.plus(step)

        var user = User(
            null,
            "guest" + id,
            "123",
            SampleDataStory.getExampleEmail(),
            SampleDataStory.getExampleAvatarUrl(),"guest"+id, GUEST,dateTimeNow())
        dbHelper.insertUser(user)
        return user
    }
    //role
    fun getRoleByRoleId(roleId: Int): Role?{
        var listOfRoles= dbHelper.getAllRoles()
        var role=listOfRoles.filter {
            it.roleID?.let { id -> id == roleId }?:false
        }.first()
        return role
    }
    fun getRoleIdMember(): Int{
        return MEMBER
    }
    //rating
    fun getRatingsByStory(storyId: Int): List<Rate> {
        return dbHelper.getRatesByStory(storyId)
    }

    fun ratingStoryByCurrentUser(rate: Rate) {

        //xoa cai cu
        dbHelper.deleteRate(rate)
        // them cai moi
        dbHelper.insertRate(rate)
    }


    //user love stories
    fun getLoveStoriesByUser(userId: Int): List<Story> {
        var storiesId = dbHelper.getLoveStoriesIdByUser(userId)
        var stories = mutableListOf<Story>()
        storiesId.forEach {
            var story = dbHelper.getStoryByStoryId(it)
            if (story != null) {
                stories.add(story)
            }
        }
        return stories
    }

    fun setUserLovedStory(userId: Int, storyId: Int) {
        //xoa cai cu
        dbHelper.deleteUserLoveStory(userId, storyId)
        dbHelper.insertUserLoveStory(userId, storyId)
    }

    fun deleteUserLovedStory(userId: Int, storyId: Int) {
        //xoa cai cu
        dbHelper.deleteUserLoveStory(userId, storyId)
    }


    //chapter history
    // use in story overview activity
    fun getChaptersHistoryByStoryAndUser(storyId: Int, userId: Int): List<Chapter> {
        var list = getChaptersHistoryByUser(userId)
        var filter = list.filter { it.storyID == storyId }
        return filter
    }

    // use by getStoriesHistoryByUser function
    fun getChaptersHistoryByUser(userId: Int): List<Chapter> {
        var listOfChaptersId = dbHelper.getChapterHistoriesIdByUser(userId)
        var listOfChapters = mutableListOf<Chapter>()
        listOfChaptersId.forEach {
            var chapter = dbHelper.getChaptersByChapterId(it)
            if (chapter != null) {
                listOfChapters.add(chapter)
            }
        }
        return listOfChapters
    }

    // use in read fragment
    fun getStoriesHistoryByUser(userId: Int): List<Story> {
        var listOfChapters = getChaptersHistoryByUser(userId)
        var listOfStoriesId = mutableListOf<Int>()
        listOfChapters.forEach {
            if (!listOfStoriesId.contains(it.storyID)) {
                //listStoryId dont have this id
                //add id to listStoryId
                listOfStoriesId.add(it.storyID)
            }
        }
        // get dataclass Story from id
        var listStories = mutableListOf<Story>()
        listOfStoriesId.forEach {
            var story = dbHelper.getStoryByStoryId(it)
            story?.let {
                listStories.add(story)

            }
        }
        return listStories
    }

    fun setChapterHistory(userId: Int, chapterId: Int) {
        // delete old data
        dbHelper.deleteChapterHistory(userId, chapterId)
        // add history
        dbHelper.insertChapterHistory(userId, chapterId)
    }

    // chapter mark
    fun getChaptersMarkedByUser(userId: Int): List<Chapter>{
        var chapterMarksId=dbHelper.getChapterMarksIdByUser(userId)
        var listOfChapters=mutableListOf<Chapter>()
        chapterMarksId.forEach{
            var chapter=dbHelper.getChaptersByChapterId(it)
            chapter?.let {
                listOfChapters.add(chapter)
            }
        }
        return listOfChapters
    }
    fun getChaptersMarkedByUserAndStory(userId: Int,storyId: Int): List<Chapter>{
        var listOfChaptersMarkedByUser=getChaptersMarkedByUser(userId)
        var listOfChapter =listOfChaptersMarkedByUser.filter { it.storyID ==storyId }
        return listOfChapter
    }
    fun setChapterMark(userId: Int,chapterId: Int){
        //xoa cai cu
        dbHelper.deleteChapterMark(userId,chapterId)
        // them cai moi
        dbHelper.insertChapterMark(userId,chapterId)
    }
    fun deleteChapterMark(userId: Int,chapterId: Int){
        dbHelper.deleteChapterMark(userId,chapterId)
    }
}