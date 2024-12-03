package com.example.worldstory.duc

import android.content.Context
import com.example.myapplication.R
import com.example.worldstory.duc.ducdataclass.DucStoryDataClass
import com.example.worldstory.duc.ducdataclass.DucChapterDataClass
import com.example.worldstory.duc.ducdataclass.DucCommentDataClass
import com.example.worldstory.duc.ducdataclass.DucGenreDataClass
import com.example.worldstory.duc.ducdataclass.DucParagraphDataClass
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.getLoremIpsumLong
import com.example.worldstory.model.Genre
import com.example.worldstory.model.Story
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object SampleDataStory {
    private val dataList= mutableListOf<DucStoryDataClass>()
    private val listOfGenre = mutableListOf<DucGenreDataClass>()
    private val listOfChapter = mutableListOf<DucChapterDataClass>()
    private val listOfParagraph = mutableListOf<DucParagraphDataClass>()
    private val listOfComment = mutableListOf<DucCommentDataClass>()
    private var sumIdComment:Int=0
    var idUser:Int get()=4
        private set(value) {}
    //    var date: String get() ="11/11/2004"
//        private set(value) {}
    var date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

    //-----------------------------------------------------
    init {
        generateData()
        generateListOfGenre()
//        generateListOfComment()
        generateListOfParagraph()
        generateListOfChapter()
    }
    fun getExampleImgURL():String{
        return             "https://drive.google.com/uc?id=1OZ0NxlZEq3x2OTgno6DJ8rH6lr1JXv8P"


    }
    fun getExampleImgURLParagraph():String{
        val p5 ="https://drive.google.com/uc?id=1zuCJMczfObSTSb50tMXeyRJkYyTLEr4y"
        return p5

    }
    fun getExampleAvatarUrl():String{
        return "https://drive.google.com/uc?id=1fPVkJqspSh0IQsQ_8teVapd5qf_q1ppV"

    }
    fun getExampleEmail(): String{
        return "example@gmail.com"
    }
    fun getOneStoryByID(context: Context,idStory:Int): DucStoryDataClass{
        return getDataList(context).filter { it.idStory==idStory }.first()
    }
    fun addData(data : DucStoryDataClass){

        dataList.add(data)
    }
    fun getDataList(context: Context): List<DucStoryDataClass>{
//        dataList.clear()
//        generateData()
        return dataList
    }
    private fun generateData(){
        val titleList = arrayOf(
            "Cau chuyen", "Xua kia", "Muon Thua Khac Ghi Nao", "HUNTER×HUNTER",
            "Khu bảo tồn khủng long", "Bất tử và Bất hạnh", "Blue Box", "Tiệm Đồ Ngọt"
        )
        val authorList = arrayOf(
            "Tran van A", "To B", "Togashi Yoshihiro", "Dira", "Eric", "Fuka", "Gahe", "Hios"
        )
        val dateList = arrayOf(
            "11/10/2024", "11/10/2024", "11/10/2024", "11/10/2024", "11/10/2024",
            "11/10/2024", "11/10/2024", "11/10/2024"
        )
        val desList = arrayOf(
            "sieu vaty ngo", "tai nang dang cap", "tim hieu qua da con trong nhu",
            "Dira dbay gio con ", "Eric nhac tgre song dong", "Fuka boi vi tinh",
            "vui khong hoi Gahe", "vi sa Hios trong mo"
        )
        val isComicList = arrayOf(
            true,true,true,true,false,false,false,false
        )
        val imgUrlListString = arrayOf(
            "https://scontent.fhan3-3.fna.fbcdn.net/v/t39.30808-6/467637825_987540789849746_543458540327297711_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=iQOcKkq2qUYQ7kNvgEgyvuo&_nc_zt=23&_nc_ht=scontent.fhan3-3.fna&_nc_gid=Aym2wPdWobnahsnjGHBJ8Oe&oh=00_AYDvl3AyQ-c3GFb3FLj7XK2t_c8cj8bY6POC_kkaENEi1A&oe=67420244",
            "https://scontent.fhan4-6.fna.fbcdn.net/v/t39.30808-6/467577331_987540779849747_3591926939402667045_n.jpg?_nc_cat=109&ccb=1-7&_nc_sid=127cfc&_nc_ohc=NhyibJOpEmAQ7kNvgEAGM7r&_nc_zt=23&_nc_ht=scontent.fhan4-6.fna&_nc_gid=AnVmve5NRl4OrLjx-d8EHne&oh=00_AYCuRMR1pjI8KRGlcie1RnAycQoSrQ86nppSJgvOwGmXyA&oe=67423357",
            "https://scontent.fhan4-3.fna.fbcdn.net/v/t39.30808-6/467696713_987540776516414_3219174740401399891_n.jpg?_nc_cat=100&ccb=1-7&_nc_sid=127cfc&_nc_ohc=_x1oBN8vZ08Q7kNvgFg-7B2&_nc_zt=23&_nc_ht=scontent.fhan4-3.fna&_nc_gid=A3p-wnaOT9q7gXqwrdGy3Hv&oh=00_AYA7bZIXbUy0YH5EeHr0a6WQYhTrJy3b_FOKLg-ytMTFAw&oe=674201D0",
            "https://scontent.fhan3-2.fna.fbcdn.net/v/t39.30808-6/467680359_987540853183073_3431101542059115151_n.jpg?_nc_cat=107&ccb=1-7&_nc_sid=127cfc&_nc_ohc=FZ3t1GKjXcsQ7kNvgHIplgn&_nc_zt=23&_nc_ht=scontent.fhan3-2.fna&_nc_gid=AE6bREnqPK2sD6wjLNDbenN&oh=00_AYActiD5cuUpUFkWB7k4un7vUW4CLgYUSmVn7EW_rTf0Dg&oe=6741FCEC",
            "https://scontent.fhan4-3.fna.fbcdn.net/v/t39.30808-6/467687540_987540869849738_578832179973704373_n.jpg?_nc_cat=110&ccb=1-7&_nc_sid=127cfc&_nc_ohc=hE8KZhy98R4Q7kNvgEmR-6G&_nc_zt=23&_nc_ht=scontent.fhan4-3.fna&_nc_gid=Ayo1347oHOkojUjtC04Fzy3&oh=00_AYDMlqHAUr8wUxWQ2FOTTQYIeethR-rT4v3agvmwvodS0w&oe=6741FFC7",
            "https://scontent.fhan4-5.fna.fbcdn.net/v/t39.30808-6/467606655_987540883183070_3596325563253202205_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=127cfc&_nc_ohc=nCgzSeoEgCsQ7kNvgGhirLp&_nc_zt=23&_nc_ht=scontent.fhan4-5.fna&_nc_gid=AYuMFUCkmXPaL0AyAlRd1Ut&oh=00_AYDbunSnUrEHDr9tLJaEBe5O3_deeWLGlx4ywpizou7Eow&oe=674205DF",
            "https://scontent.fhan3-3.fna.fbcdn.net/v/t39.30808-6/467639302_987540783183080_2145138456043692649_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=PYodwl72Ig4Q7kNvgHdlunn&_nc_zt=23&_nc_ht=scontent.fhan3-3.fna&_nc_gid=ARqyVa_1UAjU50lR0wzoyo1&oh=00_AYBQLZBtN5AE_I-ljfAe_ca1xhd1IA8nykuVvQ4MMmo5gQ&oe=67420FFC",
            "https://scontent.fhan4-6.fna.fbcdn.net/v/t39.30808-6/467618820_987540786516413_9022671729236654001_n.jpg?_nc_cat=108&ccb=1-7&_nc_sid=127cfc&_nc_ohc=wcRr4uG_frQQ7kNvgG7AO3Q&_nc_zt=23&_nc_ht=scontent.fhan4-6.fna&_nc_gid=ACWt9Nxlog48s7vciMKLvlm&oh=00_AYCFt2WZkt3Qy8rXYq0fnq2KdcuMdRdl7Qr9prRr8E71gw&oe=67420403",


            )
        val bgImgUrlListString = arrayOf(
            "https://scontent.fhan3-3.fna.fbcdn.net/v/t39.30808-6/467637825_987540789849746_543458540327297711_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=iQOcKkq2qUYQ7kNvgEgyvuo&_nc_zt=23&_nc_ht=scontent.fhan3-3.fna&_nc_gid=Aym2wPdWobnahsnjGHBJ8Oe&oh=00_AYDvl3AyQ-c3GFb3FLj7XK2t_c8cj8bY6POC_kkaENEi1A&oe=67420244",
            "https://scontent.fhan4-6.fna.fbcdn.net/v/t39.30808-6/467577331_987540779849747_3591926939402667045_n.jpg?_nc_cat=109&ccb=1-7&_nc_sid=127cfc&_nc_ohc=NhyibJOpEmAQ7kNvgEAGM7r&_nc_zt=23&_nc_ht=scontent.fhan4-6.fna&_nc_gid=AnVmve5NRl4OrLjx-d8EHne&oh=00_AYCuRMR1pjI8KRGlcie1RnAycQoSrQ86nppSJgvOwGmXyA&oe=67423357",
            "https://scontent.fhan4-3.fna.fbcdn.net/v/t39.30808-6/467696713_987540776516414_3219174740401399891_n.jpg?_nc_cat=100&ccb=1-7&_nc_sid=127cfc&_nc_ohc=_x1oBN8vZ08Q7kNvgFg-7B2&_nc_zt=23&_nc_ht=scontent.fhan4-3.fna&_nc_gid=A3p-wnaOT9q7gXqwrdGy3Hv&oh=00_AYA7bZIXbUy0YH5EeHr0a6WQYhTrJy3b_FOKLg-ytMTFAw&oe=674201D0",
            "https://scontent.fhan3-2.fna.fbcdn.net/v/t39.30808-6/467680359_987540853183073_3431101542059115151_n.jpg?_nc_cat=107&ccb=1-7&_nc_sid=127cfc&_nc_ohc=FZ3t1GKjXcsQ7kNvgHIplgn&_nc_zt=23&_nc_ht=scontent.fhan3-2.fna&_nc_gid=AE6bREnqPK2sD6wjLNDbenN&oh=00_AYActiD5cuUpUFkWB7k4un7vUW4CLgYUSmVn7EW_rTf0Dg&oe=6741FCEC",
            "https://scontent.fhan4-3.fna.fbcdn.net/v/t39.30808-6/467687540_987540869849738_578832179973704373_n.jpg?_nc_cat=110&ccb=1-7&_nc_sid=127cfc&_nc_ohc=hE8KZhy98R4Q7kNvgEmR-6G&_nc_zt=23&_nc_ht=scontent.fhan4-3.fna&_nc_gid=Ayo1347oHOkojUjtC04Fzy3&oh=00_AYDMlqHAUr8wUxWQ2FOTTQYIeethR-rT4v3agvmwvodS0w&oe=6741FFC7",
            "https://scontent.fhan4-5.fna.fbcdn.net/v/t39.30808-6/467606655_987540883183070_3596325563253202205_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=127cfc&_nc_ohc=nCgzSeoEgCsQ7kNvgGhirLp&_nc_zt=23&_nc_ht=scontent.fhan4-5.fna&_nc_gid=AYuMFUCkmXPaL0AyAlRd1Ut&oh=00_AYDbunSnUrEHDr9tLJaEBe5O3_deeWLGlx4ywpizou7Eow&oe=674205DF",
            "https://scontent.fhan3-3.fna.fbcdn.net/v/t39.30808-6/467639302_987540783183080_2145138456043692649_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=PYodwl72Ig4Q7kNvgHdlunn&_nc_zt=23&_nc_ht=scontent.fhan3-3.fna&_nc_gid=ARqyVa_1UAjU50lR0wzoyo1&oh=00_AYBQLZBtN5AE_I-ljfAe_ca1xhd1IA8nykuVvQ4MMmo5gQ&oe=67420FFC",
            "https://scontent.fhan4-6.fna.fbcdn.net/v/t39.30808-6/467618820_987540786516413_9022671729236654001_n.jpg?_nc_cat=108&ccb=1-7&_nc_sid=127cfc&_nc_ohc=wcRr4uG_frQQ7kNvgG7AO3Q&_nc_zt=23&_nc_ht=scontent.fhan4-6.fna&_nc_gid=ACWt9Nxlog48s7vciMKLvlm&oh=00_AYCFt2WZkt3Qy8rXYq0fnq2KdcuMdRdl7Qr9prRr8E71gw&oe=67420403",


            )
        val imgUrlList = arrayOf(
            R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8


        )
        val bgImgUrlList = arrayOf(
            R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4,
            R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8
        )
        val scoreList = arrayOf(3.5f, 4f, 5f, 1f, 4.5f, 1f, 4.5f, 2f)
        val idStoriesList = arrayOf(1,2,3,4,5,6,7,8)
        for(i in titleList.indices){
            val item = DucStoryDataClass(
                idStoriesList[i],
                titleList[i],
                authorList[i],
                desList[i],
                imgUrlListString[i],
                bgImgUrlListString[i],
                dateList[i],
                scoreList[i],
                isComicList[i]
            )
            addData(item)
        }
    }
    fun getExampleListOfStories(): List<Story>{
        var list=listOf<Story>()
        return list
    }
    //-----------------------------------------------------
    fun getOneGenreByID(context: Context,idGenre:Int): DucGenreDataClass{
        return getListOfGenre(context).filter { it.idGenre==idGenre }.first()
    }
    fun addGenre(item : DucGenreDataClass){
        listOfGenre.add(item)
    }
    fun getListOfGenre(context: Context): List<DucGenreDataClass>{
//        listOfGenre.clear()
//        generateListOfGenre()
        return  listOfGenre
    }
    private fun generateListOfGenre(){
        var titleList = arrayOf(
            "Chiến đấu",
            "Vui nhon",
            "Co don",

            "Tình cảm",
            "Hài hước",
            "Nhanh nhay",
            "kịch tính",
            "U mê",
            "Tu duong"

        )
        var idList = arrayOf(
            1,2,3,4

        )
        for(i in idList.indices){
            var item = DucGenreDataClass(
                idList[i],titleList[i]
            )
            addGenre(item)
        }



    }
    //------------------------------
    fun getOneChapterByID(context: Context,idChapter:Int): DucChapterDataClass{
        return getListOfChapter(context).filter { it.idChapter==idChapter }.first()

    }
    fun getOneChapter(): DucChapterDataClass{
        return DucChapterDataClass(1,1,"Chuong 1: khong gia tri","01/02/2024")
    }
    fun addChapter(item : DucChapterDataClass){
        listOfChapter.add(item)
    }
    fun getListOfChapter(context: Context): List<DucChapterDataClass>{
//        listOfChapter.clear()
//        generateListOfChapter()
        return  listOfChapter
    }
    private fun generateListOfChapter(){

        var titleList = arrayOf(
            "Chuong 1: hoho",
            "Chuong 2: hoho",
            "Chuong 3: hohoa",
            "Chuong 4: hoho e ew werwer",
            "Chuong 5: hoho sfge",
            "Chuong 6: hohodas",

            )
        var dateCraetedList = arrayOf(
            "01/02/2024",
            "01/03/2024",
            "01/04/2024",
            "01/05/2024",
            "01/06/2024",
            "01/07/2024",

            )
        var idList = arrayOf(
            1,2,3,4,5,6

        )
        var idChapter:Int=1
        for(story in dataList)
        {
            for(i in idList.indices){
                var item = DucChapterDataClass(
                    idChapter,story.idStory,titleList[i]+" ${story.idStory}",dateCraetedList[i]
                )
                idChapter++
                addChapter(item)
            }
        }




    }
    //------------------------------
    fun getOneParagraphByID(context: Context,idParagraph:Int): DucParagraphDataClass{
        return getListOfParagraph(context).filter { it.idParagraph==idParagraph }.first()
    }
    fun addParagraph(item : DucParagraphDataClass){
        listOfParagraph.add(item)
    }
    fun addParagraph(item: Array<DucParagraphDataClass> ){
        listOfParagraph.addAll(item)
    }
    fun getOneComicParagraph(): DucParagraphDataClass{
        return DucParagraphDataClass(3,getExampleImgURLParagraph(), null,1,2)
    }
    fun getOneTextParagraph(context:Context): DucParagraphDataClass{
        return DucParagraphDataClass(1,null, getLoremIpsumLong(context),1,1,false)
    }
    fun getListOfParagraph(context: Context): List<DucParagraphDataClass>{
//        listOfParagraph.clear()
//        generateListOfParagraph()
        return  listOfParagraph
    }
    private fun generateListOfParagraph(){
        val p1="https://scontent.fhan4-5.fna.fbcdn.net/v/t39.30808-6/467623087_987608846509607_7464989657181579078_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=127cfc&_nc_ohc=PJhBdDIh8ngQ7kNvgFXOx3O&_nc_zt=23&_nc_ht=scontent.fhan4-5.fna&_nc_gid=AFd7aN9gB5bxoYEwwsXIv2f&oh=00_AYD0Z_URAyRNGfnjU-LURgA-2gVde9_z-mv0bGN_9BaT5g&oe=67422259"
        val p2="https://scontent.fhan4-3.fna.fbcdn.net/v/t39.30808-6/467723438_987608853176273_4668882966082102865_n.jpg?_nc_cat=100&ccb=1-7&_nc_sid=127cfc&_nc_ohc=YWlZYUbYXkwQ7kNvgE3jYCO&_nc_zt=23&_nc_ht=scontent.fhan4-3.fna&_nc_gid=AGDIEQX_cb1_fgYK36qvGVI&oh=00_AYCdk4vBlHeH8OamHhgPsh8Krue4z4n87eTw0ze93H0qkg&oe=674228AA"
        val p3="https://scontent.fhan4-5.fna.fbcdn.net/v/t39.30808-6/467668063_987608843176274_2859379085125334494_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=127cfc&_nc_ohc=yXXD9A77qecQ7kNvgF_d8YC&_nc_zt=23&_nc_ht=scontent.fhan4-5.fna&_nc_gid=APlheJAZPnckmNTfHmSXxw1&oh=00_AYA_aNaPZsNIj4d7_m7buS-uLLEZqlrXLFmCT6tjuBpVzg&oe=6742542B"
        val p4="https://scontent.fhan3-4.fna.fbcdn.net/v/t39.30808-6/467593664_987608919842933_5614161165862365298_n.jpg?_nc_cat=104&ccb=1-7&_nc_sid=127cfc&_nc_ohc=M890QOlTAdgQ7kNvgGjskoW&_nc_zt=23&_nc_ht=scontent.fhan3-4.fna&_nc_gid=ABNzVvhlzRKGC_pZhF3yrl4&oh=00_AYDMJu16KD0Bab_Mw-oiuajQDLnUI_f6W7WFhcMHoZt7sg&oe=67423E82"
        val p5="https://scontent.fhan3-4.fna.fbcdn.net/v/t39.30808-6/467600225_987608923176266_4457027657328368414_n.jpg?_nc_cat=106&ccb=1-7&_nc_sid=127cfc&_nc_ohc=DVp19tJB7wYQ7kNvgEPu0iF&_nc_zt=23&_nc_ht=scontent.fhan3-4.fna&_nc_gid=Apt9d8aT9XV4egL6li5M-yW&oh=00_AYBUuZIL6k6Lrw2KoBPCGUFMf8MUXGL76a5nS48oD0pmJA&oe=67424E4A"
        var idParagraph:Int=1
        //so chapter hien tai cua truyen comic
        for(i in 1..24){
            var item1 = DucParagraphDataClass(idParagraph,p1, null,1,i)
            idParagraph++
            var item2 = DucParagraphDataClass(idParagraph,p2, null,2,i)
            idParagraph++
            var item3 = DucParagraphDataClass(idParagraph,p3, null,3,i)
            idParagraph++
            var item4 = DucParagraphDataClass(idParagraph,p4, null,4,i)
            idParagraph++
            var item5 = DucParagraphDataClass(idParagraph,p5, null,5,i)
            idParagraph++
            addParagraph(arrayOf(item1,item2,item3,item4,item5))

        }
        for(i in 25..48){
            var item1 = DucParagraphDataClass(idParagraph,null, getLoremIpsumLong(),1,i,false)
            idParagraph++
            var item2 = DucParagraphDataClass(idParagraph,null, getLoremIpsumLong(),1,i,false)
            idParagraph++
            var item3 = DucParagraphDataClass(idParagraph,null, getLoremIpsumLong(),1,i,false)
            idParagraph++
            var item4 = DucParagraphDataClass(idParagraph,null, getLoremIpsumLong(),1,i,false)
            idParagraph++
            addParagraph(arrayOf(item1,item2,item3,item4))
        }
//        var item1 = ParagraphDataClass(1,null, getLoremIpsumLong(),1,1,false)
//        var item2 = ParagraphDataClass(2,null, getLoremIpsumLong(),2,1,false)
//        var item3 = ParagraphDataClass(3,R.drawable.pa1, null,1,2)
//        var item4 = ParagraphDataClass(4,R.drawable.pa2, null,2,2)
//        var item5 = ParagraphDataClass(5,null, getLoremIpsumLong(),1,3,false)
//        var item6 = ParagraphDataClass(6,null, getLoremIpsumLong(),2,3,false)
//        var item7 = ParagraphDataClass(7,R.drawable.pa3, null,1,4)
//        var item8 = ParagraphDataClass(8,R.drawable.pa4, null,2,4)
//        var item9 = ParagraphDataClass(9,null, getLoremIpsumLong(),1,5,false)
//        var item10 = ParagraphDataClass(10,null, getLoremIpsumLong(),2,5,false)
//        var item11 = ParagraphDataClass(11,R.drawable.pa1, null,1,6)
//        var item12 = ParagraphDataClass(12,R.drawable.pa2, null,2,6)
//
//
//        addParagraph(arrayOf(item1,item2,item3,item4,item5,item6,item7,item8,item9,item10,item11,item12))




    }
    //---------------------------------------------------
    fun addComment(item: DucCommentDataClass ){
        listOfComment.add(item)
    }

    fun getListOfComment(context: Context): List<DucCommentDataClass>{
//        listOfComment.clear()
//        generateListOfComment()
        return  listOfComment
    }
//    private fun generateListOfComment(){
//
//        for(story in dataList)
//        {
//            for(i in 1..3){
//                var item = DucCommentDataClass(getNextIdComment() ,story.idStory,i, getLoremIpsum(),"11/11/2004")
//
//                addComment(item)
//            }
//            addComment(DucCommentDataClass(getNextIdComment(),story.idStory,idUser,getLoremIpsumLong(),date))
//
//
//            addComment(DucCommentDataClass(getNextIdComment(),story.idStory,idUser,getLoremIpsum(),date))
//
//
//            for (i in 6..8) {
//                var item = DucCommentDataClass(getNextIdComment(),story.idStory, i, getLoremIpsumLong(), "11/11/2004")
//                addComment(item)
//            }
//        }
//    }
    private fun getNextIdComment():Int{
        sumIdComment=sumIdComment+1
        return sumIdComment
    }
    fun getexampleGenre():Genre{
        return Genre(1,"Khong co du lieu",1)
    }
    fun getexampleStory(): Story{
        return Story(1,"Khong co du lieu","tac gia","mieu ta",getExampleImgURL(),getExampleImgURL(),0,
            dateTimeNow(),1f,1)

    }
}