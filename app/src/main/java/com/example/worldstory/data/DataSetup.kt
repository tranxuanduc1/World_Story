package com.example.worldstory.data

import android.database.sqlite.SQLiteDatabase
import com.example.worldstory.view.RoleEnum
import com.example.worldstory.data.dbhelper.Contract
import com.example.worldstory.data.dbhelper.DatabaseHelper
import com.example.worldstory.duc.SampleDataStory
import com.example.worldstory.duc.ducutils.dateTimeNow
import com.example.worldstory.duc.ducutils.hashPassword

fun DatabaseHelper.createDataFirstTime(db: SQLiteDatabase?) {

    //them role
    RoleEnum.values().forEach { role ->
        db?.execSQL(
            """
                INSERT INTO ${Contract.RoleEntry.TABLE_NAME} (${Contract.RoleEntry.COLUMN_NAME})
                VALUES ('${role.name}')
            """
        )
    }

    //them du lieu mac dinh
    db?.execSQL(/* sql = */ """
        INSERT INTO ${Contract.UserEntry.TABLE_NAME} 
        (${Contract.UserEntry.COLUMN_USERNAME}, ${Contract.UserEntry.COLUMN_PASSWORD}, 
         ${Contract.UserEntry.COLUMN_EMAIL}, ${Contract.UserEntry.COLUMN_NICKNAME}, 
         ${Contract.UserEntry.COLUMN_IMAGE_AVATAR}, ${Contract.UserEntry.COLUMN_CREATED_DATE}, 
         ${Contract.UserEntry.COLUMN_ROLE_ID_FK})
        VALUES 
        ('admin', '${hashPassword(SampleDataStory.getExamplePassword())}', 'admin@example.com', 'Admin',  "https://drive.google.com/uc?id=1mhFCwRj-lk34oo3tnND3A_6yGCq2aDpz", '${dateTimeNow()}', 1),
        ('author1', '${hashPassword(SampleDataStory.getExamplePassword())}', 'author1@example.com', 'Văn Thanh', "https://drive.google.com/uc?id=1fPVkJqspSh0IQsQ_8teVapd5qf_q1ppV",  '${dateTimeNow()}', 2),
        ('author2', '${hashPassword(SampleDataStory.getExamplePassword())}', 'author2@example.com', 'Tiến Linh', "https://drive.google.com/uc?id=1EKOG4MJteSF2SxLAnaWQ5-ZNu4AaeFZl",  '${dateTimeNow()}', 2),
        ('author3', '${hashPassword(SampleDataStory.getExamplePassword())}', 'author3@example.com', 'Công Pượng', "https://drive.google.com/uc?id=164A3525fCaPAZeiOhRbT6JWRAIg9Lb6Z",  '${dateTimeNow()}', 2),
        ('author4', '${hashPassword(SampleDataStory.getExamplePassword())}', 'author4@example.com', 'Đình Bắc', "https://drive.google.com/uc?id=1ZAPhqEXNCa8h8rqZSm12Dt2N6S9L2Rwa",  '${dateTimeNow()}', 2),
        ('author5', '${hashPassword(SampleDataStory.getExamplePassword())}', 'author5@example.com', 'Xuân Kha', "https://drive.google.com/uc?id=1UKV60N8QyI9ROAjsduUp6cK_ysE3XUxT",  '${dateTimeNow()}', 2),
        ('author6', '${hashPassword(SampleDataStory.getExamplePassword())}', 'author6@example.com', 'Bảo Tiến', "https://drive.google.com/uc?id=1fPVkJqspSh0IQsQ_8teVapd5qf_q1ppV",  '${dateTimeNow()}', 2),
        ('member', '${hashPassword(SampleDataStory.getExamplePassword())}', 'member@example.com', 'văn Hậu',"https://drive.google.com/uc?id=1-dvznsZJ_MEzxHP9kam5X36Zxj2Q6pVG",  '${dateTimeNow()}', 3)
        
   
    
    """
    )
    //  Thêm 7 thể loại truyện vào bảng GenreEntry
    val genres = listOf(
        "Phiêu lưu", "Hành động", "Hài hước", "Tâm lý", "Kỳ ảo", "Lãng mạn", "Kinh dị"
    )
    genres.forEach { genre ->
        db?.execSQL("""
            INSERT INTO ${Contract.GenreEntry.TABLE_NAME} 
            (${Contract.GenreEntry.COLUMN_NAME}, ${Contract.GenreEntry.COLUMN_USER_CREATED_ID_FK})
            VALUES ('$genre', 1)
        """)
    }
    // 2. Thêm 20 bộ truyện manga vào bảng StoryEntry
    val mangaStories = listOf(
        "One Piece", "Naruto", "Dragon Ball", "Attack on Titan", "Demon Slayer",
        "My Hero Academia", "Hunter x Hunter", "Death Note", "Sword Art Online", "Bleach",
        "Tokyo Ghoul", "Fullmetal Alchemist", "Fairy Tail", "Black Clover", "One Punch Man",
        "JoJos Bizarre Adventure", "Detective Conan", "Re:Zero", "Gintama", "Chainsaw Man"
    )

    val authors = listOf(
        "Eiichiro Oda",        // One Piece
        "Masashi Kishimoto",   // Naruto
        "Akira Toriyama",      // Dragon Ball
        "Hajime Isayama",      // Attack on Titan
        "Koyoharu Gotouge",    // Demon Slayer
        "Kohei Horikoshi",     // My Hero Academia
        "Yoshihiro Togashi",   // Hunter x Hunter
        "Tsugumi Ohba",        // Death Note
        "Reki Kawahara",       // Sword Art Online
        "Tite Kubo",           // Bleach
        "Sui Ishida",          // Tokyo Ghoul
        "Hiromu Arakawa",      // Fullmetal Alchemist
        "Hiro Mashima",        // Fairy Tail
        "Yūki Tabata",         // Black Clover
        "ONE",                 // One Punch Man
        "Hirohiko Araki",      // JoJo's Bizarre Adventure
        "Gosho Aoyama",        // Detective Conan
        "Tappei Nagatsuki",    // Re:Zero
        "Yoshihiro Togashi",   // Gintama
        "Tatsuki Fujimoto"      // Chainsaw Man
    )
    val descriptions = listOf(
        "Một cậu bé cướp biển tên là Monkey D. Luffy bắt đầu hành trình tìm kiếm kho báu huyền thoại One Piece.",
        "Câu chuyện xoay quanh Naruto Uzumaki, một ninja trẻ mơ ước trở thành ninja mạnh nhất và lãnh đạo làng của mình.",
        "Theo chân Goku trong những cuộc phiêu lưu khi cậu tập luyện võ thuật và khám phá thế giới để tìm kiếm các viên ngọc rồng.",
        "Cuộc chiến của nhân loại để sinh tồn trước những sinh vật khổng lồ được gọi là Titan.",
        "Một cậu bé tên Tanjiro Kamado trở thành người diệt quỷ để trả thù cho gia đình và cứu em gái của mình.",
        "Trong một thế giới có anh hùng và kẻ phản diện, một cậu bé không có sức mạnh cố gắng trở thành một anh hùng.",
        "Theo chân Gon Freecss, một cậu bé trở thành Hunter để tìm kiếm cha mình.",
        "Một câu chuyện tâm lý hồi hộp về một học sinh trung học phát hiện ra một cuốn sổ có thể giết bất kỳ ai có tên được viết vào đó.",
        "Một câu chuyện về những người chơi bị mắc kẹt trong một trò chơi MMORPG thực tế ảo và cuộc chiến để thoát ra.",
        "Theo chân Ichigo Kurosaki, một thiếu niên trở thành Soul Reaper và chiến đấu chống lại các linh hồn ác.",
        "Một câu chuyện giả tưởng đen tối về một sinh viên đại học bị cuốn vào thế giới của những con quỷ ăn thịt người.",
        "Một câu chuyện về giả kim thuật, theo chân hai anh em tìm kiếm Viên đá triết gia để phục hồi cơ thể của họ.",
        "Một câu chuyện về một nhóm phù thủy và những cuộc phiêu lưu của họ trong một thế giới ma thuật.",
        "Một cậu bé phát hiện ra mình có sức mạnh ma thuật và gia nhập một hội phù thủy.",
        "Theo chân Saitama, một anh hùng có thể đánh bại bất kỳ đối thủ nào chỉ bằng một cú đấm, dẫn đến cuộc khủng hoảng tồn tại.",
        "Một câu chuyện đa thế hệ về gia đình Joestar và những cuộc chiến của họ chống lại kẻ thù siêu nhiên.",
        "Một câu chuyện trinh thám về một học sinh trung học giải quyết các vụ án trong khi giữ bí mật về danh tính của mình.",
        "Một loạt phim giả tưởng về một cậu bé bị đưa đến một thế giới khác và phải đối mặt với những thử thách của nó.",
        "Một loạt phim hài và hành động về một nhóm bạn và những cuộc phiêu lưu của họ.",
        "Một loạt phim kinh dị hành động về một người trẻ trở thành thợ săn quỷ để trả nợ cho cha đã khuất của mình."
    )
    var imgUrls = listOf(
        "https://drive.google.com/uc?id=1bRyTkaiw7Vq4z0A5F10jv1TyjpNHids1",
        "https://drive.google.com/uc?id=1Km_vjhBZ7NagHrfxn6VcNkT0Uldf5w-G",
        "https://drive.google.com/uc?id=1aiZhFsgxMcHnvHu7f9bEKbCaSXbm5sgH",
        "https://drive.google.com/uc?id=1D30yOSXufuubReMfTxP6h3wMExFeM090",
        "https://drive.google.com/uc?id=1s4A9e1WVPp-VKMCChYnf6vid0pcJZPR4",
        "https://drive.google.com/uc?id=1A_CNDvtoPjUFv3BYGu1vQ6iBoj9tmdvm",
        "https://drive.google.com/uc?id=1s8Ogazo_94ab6cnQ-ipOAAd3eMjnwWH9",
        "https://drive.google.com/uc?id=1S0AbF7knzNf9wFJawP9Kszl5HMJ76bwa",
        "https://drive.google.com/uc?id=1qUYLwfa77GjtFO2DPBZfqQylkengR-Aw",
        "https://drive.google.com/uc?id=1l0QSoPxlCnhuaXXAthpcF1ttR25phgcQ",
        "https://drive.google.com/uc?id=1czvEvDtSAlMS9lv49cazE7dzBadlhiCK",
        "https://drive.google.com/uc?id=1ZcE1jOu8eOEpdAZkWyVAruHYyp90oTbr",
        "https://drive.google.com/uc?id=1FQq3MPNj0KpeL-GDQ9xiHc_reELuLQvH",
        "https://drive.google.com/uc?id=1Vt1-vqQQ77DPQ_Kmp_0Mj97iplZC8cnN",
        "https://drive.google.com/uc?id=1Ih80ubHNjk9bBqKyPtX4Guij22mD8lOa",
        "https://drive.google.com/uc?id=1HlqL3zJrvh-fVMBNQSNHuLhk0vVhbPps",
        "https://drive.google.com/uc?id=1tv_ixgkJw9N_tXjBUIl4AD7nRxejsxiA",
        "https://drive.google.com/uc?id=15tAJf4r08XrA8wXt4V-j66dGunj_d7a5",
        "https://drive.google.com/uc?id=1qaW7-WN94Ht7bffaj5gU8iJsyfdIo0rH",
        "https://drive.google.com/uc?id=1XmHvb9E2qLwFrskcr6ra1_bwZ9puXDhG"
    )
    imgUrls=imgUrls.reversed()
    var bgImgUrls = listOf(
        "https://drive.google.com/uc?id=12pB3TAEwJbvJ9yC1nXNTt1oGdk_nm17j",
        "https://drive.google.com/uc?id=1wt8mQmHmvE7sNz0juxnqWfpVj6X7Hxlv",
        "https://drive.google.com/uc?id=1j_rwX_VF3GgKMJbrVvLP44G8wLkkv-_j",
        "https://drive.google.com/uc?id=1RWAgTjv-C_CVhlUJj4MJECxKwV4uFZuP",
        "https://drive.google.com/uc?id=1d8WyxLUxW8iqqKgQ_twezWKDyYcsgMZg",
        "https://drive.google.com/uc?id=1aWZoKVrlW_B8T3TSdj44FZ8DV0AN_TaQ",
        "https://drive.google.com/uc?id=1VACGeD5BTcQNqVcnE037aVVobHYSjCu8",
        "https://drive.google.com/uc?id=1QFZElNOosZxoJmPDOd8B5N5RH1A_5srn",
        "https://drive.google.com/uc?id=16mIQwv_PhAChxZeHbo-m0170CQlAZGjm",
        "https://drive.google.com/uc?id=1WiUWSjsSaAJjDDu12e_cmnJ4kE1ziSh0",
        "https://drive.google.com/uc?id=1Iq0lJ7eYBoyTIkB1vsVJjEREzfCrtAdR",
        "https://drive.google.com/uc?id=18Z5tK1d3QvsBkJ21OWBtrAF8NAYR0bqw",
        "https://drive.google.com/uc?id=1wWDWlBFEvJ5VW7JZP07nyNCXrsAH8YGe",
        "https://drive.google.com/uc?id=1ou_LyisHTk9AC2K5f0yVorWdZu3JV_ty",
        "https://drive.google.com/uc?id=1UuoYzDU0O_Fd76wvFXU_ZxpK9vgLsmo9",
        "https://drive.google.com/uc?id=1jviVQIhamiw5hgB1AmBW27pyqMWk0XBj",
        "https://drive.google.com/uc?id=1egaWmKwnGmCiw2iS7fUA50Ji-M5vtmEP",
        "https://drive.google.com/uc?id=1nybyWWUJEQa0FLnVaPDZK-CZajqSbDRC",
        "https://drive.google.com/uc?id=16No7HNhN9qKcaI4hlW3DINc3a796hjQb",
        "https://drive.google.com/uc?id=1zSbVNgEP6ygIp0E3UoVuY1BxQmFwFKIE"
    )
    bgImgUrls=bgImgUrls.reversed()
    mangaStories.forEachIndexed { index, title ->
        val randomUserId = listOf(2, 3, 4, 5, 6).random() // Chọn ngẫu nhiên từ danh sách user ID
        val createdDate = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date()) // Lấy ngày hiện tại
        db?.execSQL("""
            INSERT INTO ${Contract.StoryEntry.TABLE_NAME}
            (${Contract.StoryEntry.COLUMN_TITLE}, ${Contract.StoryEntry.COLUMN_AUTHOR},
             ${Contract.StoryEntry.COLUMN_DESCRIPTION}, ${Contract.StoryEntry.COLUMN_IMAGE_URL},
             ${Contract.StoryEntry.COLUMN_BACKGROUND_IMAGE_URL}, ${Contract.StoryEntry.COLUMN_CREATED_DATE},
             ${Contract.StoryEntry.COLUMN_SCORE}, ${Contract.StoryEntry.COLUMN_IS_TEXT_STORY},
             ${Contract.StoryEntry.COLUMN_USER_CREATED_ID_FK})
            VALUES ('$title', '${authors[index]}', '${descriptions[index]}', '${imgUrls[index]}',
                    '${bgImgUrls[index]}', '${dateTimeNow()}', 5, 0, $randomUserId)
        """)
    }

    val textstories = listOf(
        "Tam Quốc Diễn Nghĩa",
        "Tây Du Ký",
        "Thủy Hử",
        "Hồng Lâu Mộng",
        "Đạo Mộ Bút Ký",
        "Mạn Châu Sa Hoa",
        "Tru Tiên",
        "Phàm Nhân Tu Tiên",
        "Tuyệt Thế Đường Môn",
        "Đấu Phá Thương Khung",
        "Thần Mộ",
        "Bàn Long",
        "Đấu La Đại Lục",
        "Hậu Cung Như Ý Truyện",
        "Nhất Niệm Vĩnh Hằng",
        "Tân Hoa Đồ",
        "Thương Thiên",
        "Ma Thiên Ký",
        "Thiên Long Bát Bộ",
        "Thần Điêu Đại Hiệp"
    )

    val textAuthors = listOf(
        "La Quán Trung",         // Tam Quốc Diễn Nghĩa
        "Ngô Thừa Ân",           // Tây Du Ký
        "Thi Nại Am",            // Thủy Hử
        "Tào Tuyết Cần",         // Hồng Lâu Mộng
        "Nam Phái Tam Thúc",     // Đạo Mộ Bút Ký
        "Đông Hoa Tiên Quân",    // Mạn Châu Sa Hoa
        "Tiêu Đỉnh",             // Tru Tiên
        "Vong Ngữ",              // Phàm Nhân Tu Tiên
        "Đường Gia Tam Thiếu",   // Tuyệt Thế Đường Môn
        "Thiên Tàm Thổ Đậu",     // Đấu Phá Thương Khung
        "Thần Đông",             // Thần Mộ
        "Ngã Cật Tây Hồng Thị",  // Bàn Long
        "Đường Gia Tam Thiếu",   // Đấu La Đại Lục
        "Lưu Liễm Tử",           // Hậu Cung Như Ý Truyện
        "Nhĩ Căn",               // Nhất Niệm Vĩnh Hằng
        "Vũ Nham",               // Tân Hoa Đồ
        "Phong Lộng",            // Thương Thiên
        "Ngã Cật Tây Hồng Thị",  // Ma Thiên Ký
        "Kim Dung",              // Thiên Long Bát Bộ
        "Kim Dung"               // Thần Điêu Đại Hiệp
    )

    var textDescriptions = listOf(
        "Một tác phẩm kinh điển kể về thời kỳ Tam Quốc đầy biến động, với những trận chiến và mưu kế huyền thoại.",
        "Hành trình thỉnh kinh của Đường Tăng cùng các đồ đệ trong thế giới đầy quỷ dữ và thần tiên.",
        "Câu chuyện về 108 anh hùng Lương Sơn Bạc và cuộc chiến chống lại triều đình.",
        "Một câu chuyện tình yêu và gia tộc đầy bi kịch trong xã hội phong kiến Trung Quốc.",
        "Hành trình khám phá các bí mật cổ mộ với những âm mưu, kho báu và lời nguyền.",
        "Một câu chuyện tình yêu vượt thời gian giữa các nhân vật huyền thoại trong truyền thuyết.",
        "Cuộc chiến giữa con người, yêu ma và thần tiên trong một thế giới kỳ ảo.",
        "Câu chuyện về hành trình tu tiên của một người phàm đầy gian nan và thách thức.",
        "Cuộc phiêu lưu đầy cảm xúc của một nhóm anh hùng trong thế giới huyền huyễn.",
        "Câu chuyện về một thiếu niên từ vô danh đến đỉnh cao võ thuật trong một thế giới đầy hiểm nguy.",
        "Một câu chuyện về sự sống, cái chết và tái sinh trong thế giới tu tiên.",
        "Cuộc phiêu lưu của một thiếu niên trong hành trình tìm kiếm sức mạnh tối thượng.",
        "Một câu chuyện về tinh thần đồng đội và chiến đấu trong thế giới đầy hồn hoàn.",
        "Câu chuyện về cuộc sống và mưu cầu quyền lực trong hậu cung thời phong kiến.",
        "Cuộc phiêu lưu vượt thời gian và không gian để đạt được sự vĩnh hằng.",
        "Một tác phẩm huyền huyễn kể về những âm mưu và sức mạnh bí ẩn.",
        "Một câu chuyện tình yêu và quyền lực trong thế giới đầy xung đột.",
        "Cuộc phiêu lưu kỳ ảo và chiến đấu chống lại cái ác trong một thế giới rộng lớn.",
        "Một kiệt tác võ hiệp kinh điển của Kim Dung với những trận đấu võ và mối tình bi kịch.",
        "Câu chuyện tình yêu và võ thuật giữa Dương Quá và Tiểu Long Nữ."
    )

    textstories.forEachIndexed { index, title ->
        val randomUserId = listOf(2, 3, 4, 5, 6).random() // Chọn ngẫu nhiên từ danh sách user ID
        db?.execSQL("""
            INSERT INTO ${Contract.StoryEntry.TABLE_NAME}
            (${Contract.StoryEntry.COLUMN_TITLE}, ${Contract.StoryEntry.COLUMN_AUTHOR},
             ${Contract.StoryEntry.COLUMN_DESCRIPTION}, ${Contract.StoryEntry.COLUMN_IMAGE_URL},
             ${Contract.StoryEntry.COLUMN_BACKGROUND_IMAGE_URL}, ${Contract.StoryEntry.COLUMN_CREATED_DATE},
             ${Contract.StoryEntry.COLUMN_SCORE}, ${Contract.StoryEntry.COLUMN_IS_TEXT_STORY},
             ${Contract.StoryEntry.COLUMN_USER_CREATED_ID_FK})
            VALUES ('$title', '${textAuthors[index]}', '${textDescriptions[index]}', '${imgUrls[index]}',
                    '${bgImgUrls[index]}', '${dateTimeNow()}', 5, 1, $randomUserId)
        """)
    }

    val random = java.util.Random()

    for (storyId in 1..40) {
        // chon 1->4 the loai cho moi story
        val genreCount = random.nextInt(4) + 1
        val selectedGenres = mutableSetOf<Int>()

        // chon ngau nhien the loai cho story ma khong trung
        while (selectedGenres.size < genreCount) {
            val genreId = random.nextInt(7) + 1
            selectedGenres.add(genreId)
        }

        // them vao db
        selectedGenres.forEach { genreId ->
            db?.execSQL("""
            INSERT INTO ${Contract.StoryGenreEntry.TABLE_NAME} 
            (${Contract.StoryGenreEntry.COLUMN_STORY_ID_FK}, ${Contract.StoryGenreEntry.COLUMN_GENRE_ID_FK}) 
            VALUES ($storyId, $genreId)
        """.trimIndent())
        }

    }
    for (storyId in 1..10) {

        //them chapter

        for (chapterId in 1..5) {
            val chapterTitle = "Chương $chapterId truyen $storyId"
            val dateCreated = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())

            // Chèn dữ liệu chapter vào bảng chapter_table
            db?.execSQL("""
                    INSERT INTO ${Contract.ChapterEntry.TABLE_NAME} 
                    (${Contract.ChapterEntry.COLUMN_TITLE}, ${Contract.ChapterEntry.COLUMN_DATE_CREATED}, 
                     ${Contract.ChapterEntry.COLUMN_STORY_ID_FK})
                    VALUES ('$chapterTitle', '$dateCreated', $storyId)
                """)



        }
    }
    for (storyId in 21..30) {

        //them chapter

        for (chapterId in 1..5) {
            val chapterTitle = "Chương $chapterId truyen $storyId"
            val dateCreated = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())

            // Chèn dữ liệu chapter vào bảng chapter_table
            db?.execSQL("""
                    INSERT INTO ${Contract.ChapterEntry.TABLE_NAME} 
                    (${Contract.ChapterEntry.COLUMN_TITLE}, ${Contract.ChapterEntry.COLUMN_DATE_CREATED}, 
                     ${Contract.ChapterEntry.COLUMN_STORY_ID_FK})
                    VALUES ('$chapterTitle', '$dateCreated', $storyId)
                """)



        }
    }

    val step = 5

    val a = (1..50 step step).toList()
    val b = (2..50 step step).toList()
    val c = (3..50 step step).toList()
    val d = (4..50 step step).toList()
    val e = (5..50 step step).toList()

    val i1=listOf<String>(
        "https://drive.google.com/uc?id=1iKBgjMHw73OAytl8Run4d_2oQWL4eQEz",
        "https://drive.google.com/uc?id=1AGQ-8vJM63LgX2mftNKgsAllvgfRk3PC",
        "https://drive.google.com/uc?id=1aR0RpxtLg3837T_qKG5EVaV5FZ8Ws4f6",
        "https://drive.google.com/uc?id=1EyHe_ttla2SudbPJnC_GpEQKC9YG8w59",
        "https://drive.google.com/uc?id=11N8CFRv6-8pUZGA6dBA0RDlsQo7jX5Rx",
    )
    val i2=listOf<String>(
        "https://drive.google.com/uc?id=1eakvmaORdnsqh2qwqKWu-SCOs-gjtLdw",
        "https://drive.google.com/uc?id=1_0aORA9dNAZ7q8NLpLET0106CnkHkfaT",
        "https://drive.google.com/uc?id=1KIW78-XpmWOmVwjwui8e0nzWznCZxqIJ",
        "https://drive.google.com/uc?id=1esD2ena8DXZ2754nXfLDqhRvCNzO_y1d",
        "https://drive.google.com/uc?id=1NxN-jMkOrP3GyhBugBvs47v6-f25e_5R",
    )
    val i3=listOf<String>(
        "https://drive.google.com/uc?id=1SjtGjd4_EPA4BgASitrDZ68zQ9RoKD0W",
        "https://drive.google.com/uc?id=1c0taXa9rMZ6XCpUYMQYkeSYD5_Amsirv",
        "https://drive.google.com/uc?id=1i1PqOWyRQrC1YnBBCYhwIjo4Pjtuymzv",
        "https://drive.google.com/uc?id=1l7Cpk9ab3cojsvHDQPY1jw1ygpuP0VbW",
        "https://drive.google.com/uc?id=1kN99pqcIwlezJNX1ye5Sf-jSeG4NLtit",
    )
    val i4=listOf<String>(
        "https://drive.google.com/uc?id=1K4FfQU9qjn5ln347AgqO3lyKPvemD_BC",
        "https://drive.google.com/uc?id=1Z-6kRVl7L_fMMfTt8bHyCiHPQR7RzQX0",
        "https://drive.google.com/uc?id=1cvPhmmD1X18aolmbLRTjCuFOfmdTS6JP",
        "https://drive.google.com/uc?id=12V9oRlRsLkhAVOFSb2UIqolV4qYl2-kW",
        "https://drive.google.com/uc?id=1Q-Fzr9vHHJFpCw6sp38vMIYKXg0FQUnO",
    )
    val i5=listOf<String>(
        "https://drive.google.com/uc?id=1JNEAb_iOtRTyOhsd1POlm3vOYxPCj3CG",
        "https://drive.google.com/uc?id=1Gl4pPxFjXNLWeSIOo5vIbeZ__q82arAv",
        "https://drive.google.com/uc?id=1sTk-ml8qXbbF8-J1nxqezt1yOzem00ul",
        "https://drive.google.com/uc?id=1p5XhOnSc2LR5bndBX_ZjtkRiPAtE1Z0N",
        "https://drive.google.com/uc?id=19gHQBpKIY0WC3p_pwaHCgpJT0DYNNX4R"
    )
    var listOfImageChapterUrl=listOf<String>(
        "https://drive.google.com/uc?id=1eFvesLiPiREI8q8B2EAufHPWYv5D_jul",
        "https://drive.google.com/uc?id=1qcKZ_12g0qdq1wO9kSAzjf3vb4geWepj",
        "https://drive.google.com/uc?id=1UZH_gJMCENWPALjWC7--ZFvFoSZUINWZ",
        "https://drive.google.com/uc?id=1Tuc_sSJgZqdT2ip_54xISNXS2xu3nVxv",
        "https://drive.google.com/uc?id=1zuCJMczfObSTSb50tMXeyRJkYyTLEr4y",
    )
    a.forEach{chapterId->
        for (image in 0..(i1.size-1)) {


            val order = image

            // Chèn dữ liệu hình ảnh vào bảng img_table
            db?.execSQL("""
                    INSERT INTO ${Contract.ImageEntry.TABLE_NAME} 
                    (${Contract.ImageEntry.COLUMN_CONTENT_FILE}, ${Contract.ImageEntry.COLUMN_NUMBER_ORDER}, 
                     ${Contract.ImageEntry.COLUMN_CHAPTER_ID_FK})
                    VALUES ('${i1[image]}', $order, $chapterId)
                """)
        }
    }
    b.forEach{chapterId->
        for (image in 0..(i2.size-1)) {


            val order = image

            // Chèn dữ liệu hình ảnh vào bảng img_table
            db?.execSQL("""
                    INSERT INTO ${Contract.ImageEntry.TABLE_NAME} 
                    (${Contract.ImageEntry.COLUMN_CONTENT_FILE}, ${Contract.ImageEntry.COLUMN_NUMBER_ORDER}, 
                     ${Contract.ImageEntry.COLUMN_CHAPTER_ID_FK})
                    VALUES ('${i2[image]}', $order, $chapterId)
                """)
        }
    }
    c.forEach{chapterId->
        for (image in 0..(i3.size-1)) {


            val order = image

            // Chèn dữ liệu hình ảnh vào bảng img_table
            db?.execSQL("""
                    INSERT INTO ${Contract.ImageEntry.TABLE_NAME} 
                    (${Contract.ImageEntry.COLUMN_CONTENT_FILE}, ${Contract.ImageEntry.COLUMN_NUMBER_ORDER}, 
                     ${Contract.ImageEntry.COLUMN_CHAPTER_ID_FK})
                    VALUES ('${i3[image]}', $order, $chapterId)
                """)
        }
    }
    d.forEach{chapterId->
        for (image in 0..(i4.size-1)) {


            val order = image

            // Chèn dữ liệu hình ảnh vào bảng img_table
            db?.execSQL("""
                    INSERT INTO ${Contract.ImageEntry.TABLE_NAME} 
                    (${Contract.ImageEntry.COLUMN_CONTENT_FILE}, ${Contract.ImageEntry.COLUMN_NUMBER_ORDER}, 
                     ${Contract.ImageEntry.COLUMN_CHAPTER_ID_FK})
                    VALUES ('${i4[image]}', $order, $chapterId)
                """)
        }
    }
    e.forEach{chapterId->
        for (image in 0..(i5.size-1)) {


            val order = image

            // Chèn dữ liệu hình ảnh vào bảng img_table
            db?.execSQL("""
                    INSERT INTO ${Contract.ImageEntry.TABLE_NAME} 
                    (${Contract.ImageEntry.COLUMN_CONTENT_FILE}, ${Contract.ImageEntry.COLUMN_NUMBER_ORDER}, 
                     ${Contract.ImageEntry.COLUMN_CHAPTER_ID_FK})
                    VALUES ('${i5[image]}', $order, $chapterId)
                """)
        }
    }

    val a1 = (51..100 step step).toList()
    val b1 = (52..100 step step).toList()
    val c1 = (53..100 step step).toList()
    val d1 = (54..100 step step).toList()
    val e1 = (55..100 step step).toList()

    val i11 = listOf(
        "Lorem ipsum odor amet, consectetuer adipiscing elit. Faucibus habitasse conubia quis primis metus. Sit facilisi blandit elit convallis et aenean. Tincidunt vestibulum sem tempor facilisi lacinia. Auctor aptent nascetur nibh consectetur neque condimentum. Praesent ultricies porta nec curabitur vivamus posuere vel cubilia nisi? Faucibus accumsan proin ligula; fames quisque consectetur inceptos aliquet. Luctus molestie nec turpis ligula fermentum porttitor cubilia. In pellentesque tortor auctor eu mollis varius purus ligula.",
        "Amet rhoncus posuere posuere volutpat ultrices. Mattis euismod augue efficitur ut tellus ante. Pharetra molestie blandit ultricies blandit semper faucibus taciti nec. Nisl condimentum purus sed amet laoreet himenaeos. Pellentesque volutpat suspendisse quis ut dis. Mi habitasse eu elit hendrerit litora pellentesque tincidunt turpis. Purus orci himenaeos erat duis lorem et sodales penatibus velit.",
        "Donec vitae odio; adipiscing inceptos orci dis torquent. Parturient commodo vivamus arcu finibus libero montes hac ac. Pretium fusce placerat nunc primis placerat dis pretium. Augue vestibulum felis venenatis vehicula; proin at a? Erat ornare etiam primis euismod tempus lobortis. Ipsum molestie et nibh justo ipsum dolor ac cubilia. Facilisis litora semper leo fames blandit blandit. Porttitor conubia nam dis placerat purus faucibus laoreet. Duis bibendum feugiat netus parturient mus, congue potenti himenaeos.",
        "Efficitur vivamus tincidunt proin pulvinar elit vestibulum tristique fermentum. Mattis velit class turpis euismod per senectus amet. Consectetur velit proin vel at in, aliquet commodo arcu. Tincidunt parturient porta ipsum ullamcorper felis aliquam non. Sapien dapibus eleifend fames mi iaculis rhoncus nisi. Arcu dolor massa molestie euismod orci maecenas consequat senectus ornare.",
        "Semper maximus aenean blandit sed scelerisque mollis velit fusce lacinia. Habitant fermentum vitae ultrices vehicula dolor. Etiam malesuada praesent et libero cubilia. Est lacinia nunc conubia nullam proin lobortis quis eros. Ultrices efficitur ridiculus suscipit himenaeos platea. Et litora potenti inceptos phasellus auctor sed risus nec interdum. Luctus sollicitudin ex sagittis egestas faucibus lectus hac diam. Leo praesent aptent nulla pretium, torquent rutrum posuere per."
    )

    val i22 = listOf(
        "Lorem ipsum odor amet, consectetuer adipiscing elit. Faucibus habitasse conubia quis primis metus. Sit facilisi blandit elit convallis et aenean. Tincidunt vestibulum sem tempor facilisi lacinia. Auctor aptent nascetur nibh consectetur neque condimentum. Praesent ultricies porta nec curabitur vivamus posuere vel cubilia nisi? Faucibus accumsan proin ligula; fames quisque consectetur inceptos aliquet. Luctus molestie nec turpis ligula fermentum porttitor cubilia. In pellentesque tortor auctor eu mollis varius purus ligula.",
        "Amet rhoncus posuere posuere volutpat ultrices. Mattis euismod augue efficitur ut tellus ante. Pharetra molestie blandit ultricies blandit semper faucibus taciti nec. Nisl condimentum purus sed amet laoreet himenaeos. Pellentesque volutpat suspendisse quis ut dis. Mi habitasse eu elit hendrerit litora pellentesque tincidunt turpis. Purus orci himenaeos erat duis lorem et sodales penatibus velit.",
        "Donec vitae odio; adipiscing inceptos orci dis torquent. Parturient commodo vivamus arcu finibus libero montes hac ac. Pretium fusce placerat nunc primis placerat dis pretium. Augue vestibulum felis venenatis vehicula; proin at a? Erat ornare etiam primis euismod tempus lobortis. Ipsum molestie et nibh justo ipsum dolor ac cubilia. Facilisis litora semper leo fames blandit blandit. Porttitor conubia nam dis placerat purus faucibus laoreet. Duis bibendum feugiat netus parturient mus, congue potenti himenaeos.",
        "Efficitur vivamus tincidunt proin pulvinar elit vestibulum tristique fermentum. Mattis velit class turpis euismod per senectus amet. Consectetur velit proin vel at in, aliquet commodo arcu. Tincidunt parturient porta ipsum ullamcorper felis aliquam non. Sapien dapibus eleifend fames mi iaculis rhoncus nisi. Arcu dolor massa molestie euismod orci maecenas consequat senectus ornare.",
        "Semper maximus aenean blandit sed scelerisque mollis velit fusce lacinia. Habitant fermentum vitae ultrices vehicula dolor. Etiam malesuada praesent et libero cubilia. Est lacinia nunc conubia nullam proin lobortis quis eros. Ultrices efficitur ridiculus suscipit himenaeos platea. Et litora potenti inceptos phasellus auctor sed risus nec interdum. Luctus sollicitudin ex sagittis egestas faucibus lectus hac diam. Leo praesent aptent nulla pretium, torquent rutrum posuere per."
    )

    val i33 = listOf(
        "Lorem ipsum odor amet, consectetuer adipiscing elit. Faucibus habitasse conubia quis primis metus. Sit facilisi blandit elit convallis et aenean. Tincidunt vestibulum sem tempor facilisi lacinia. Auctor aptent nascetur nibh consectetur neque condimentum. Praesent ultricies porta nec curabitur vivamus posuere vel cubilia nisi? Faucibus accumsan proin ligula; fames quisque consectetur inceptos aliquet. Luctus molestie nec turpis ligula fermentum porttitor cubilia. In pellentesque tortor auctor eu mollis varius purus ligula.",
        "Amet rhoncus posuere posuere volutpat ultrices. Mattis euismod augue efficitur ut tellus ante. Pharetra molestie blandit ultricies blandit semper faucibus taciti nec. Nisl condimentum purus sed amet laoreet himenaeos. Pellentesque volutpat suspendisse quis ut dis. Mi habitasse eu elit hendrerit litora pellentesque tincidunt turpis. Purus orci himenaeos erat duis lorem et sodales penatibus velit.",
        "Donec vitae odio; adipiscing inceptos orci dis torquent. Parturient commodo vivamus arcu finibus libero montes hac ac. Pretium fusce placerat nunc primis placerat dis pretium. Augue vestibulum felis venenatis vehicula; proin at a? Erat ornare etiam primis euismod tempus lobortis. Ipsum molestie et nibh justo ipsum dolor ac cubilia. Facilisis litora semper leo fames blandit blandit. Porttitor conubia nam dis placerat purus faucibus laoreet. Duis bibendum feugiat netus parturient mus, congue potenti himenaeos.",
        "Efficitur vivamus tincidunt proin pulvinar elit vestibulum tristique fermentum. Mattis velit class turpis euismod per senectus amet. Consectetur velit proin vel at in, aliquet commodo arcu. Tincidunt parturient porta ipsum ullamcorper felis aliquam non. Sapien dapibus eleifend fames mi iaculis rhoncus nisi. Arcu dolor massa molestie euismod orci maecenas consequat senectus ornare.",
        "Semper maximus aenean blandit sed scelerisque mollis velit fusce lacinia. Habitant fermentum vitae ultrices vehicula dolor. Etiam malesuada praesent et libero cubilia. Est lacinia nunc conubia nullam proin lobortis quis eros. Ultrices efficitur ridiculus suscipit himenaeos platea. Et litora potenti inceptos phasellus auctor sed risus nec interdum. Luctus sollicitudin ex sagittis egestas faucibus lectus hac diam. Leo praesent aptent nulla pretium, torquent rutrum posuere per."
    )

    val i44 = listOf(
        "Lorem ipsum odor amet, consectetuer adipiscing elit. Faucibus habitasse conubia quis primis metus. Sit facilisi blandit elit convallis et aenean. Tincidunt vestibulum sem tempor facilisi lacinia. Auctor aptent nascetur nibh consectetur neque condimentum. Praesent ultricies porta nec curabitur vivamus posuere vel cubilia nisi? Faucibus accumsan proin ligula; fames quisque consectetur inceptos aliquet. Luctus molestie nec turpis ligula fermentum porttitor cubilia. In pellentesque tortor auctor eu mollis varius purus ligula.",
        "Amet rhoncus posuere posuere volutpat ultrices. Mattis euismod augue efficitur ut tellus ante. Pharetra molestie blandit ultricies blandit semper faucibus taciti nec. Nisl condimentum purus sed amet laoreet himenaeos. Pellentesque volutpat suspendisse quis ut dis. Mi habitasse eu elit hendrerit litora pellentesque tincidunt turpis. Purus orci himenaeos erat duis lorem et sodales penatibus velit.",
        "Donec vitae odio; adipiscing inceptos orci dis torquent. Parturient commodo vivamus arcu finibus libero montes hac ac. Pretium fusce placerat nunc primis placerat dis pretium. Augue vestibulum felis venenatis vehicula; proin at a? Erat ornare etiam primis euismod tempus lobortis. Ipsum molestie et nibh justo ipsum dolor ac cubilia. Facilisis litora semper leo fames blandit blandit. Porttitor conubia nam dis placerat purus faucibus laoreet. Duis bibendum feugiat netus parturient mus, congue potenti himenaeos.",
        "Efficitur vivamus tincidunt proin pulvinar elit vestibulum tristique fermentum. Mattis velit class turpis euismod per senectus amet. Consectetur velit proin vel at in, aliquet commodo arcu. Tincidunt parturient porta ipsum ullamcorper felis aliquam non. Sapien dapibus eleifend fames mi iaculis rhoncus nisi. Arcu dolor massa molestie euismod orci maecenas consequat senectus ornare.",
        "Semper maximus aenean blandit sed scelerisque mollis velit fusce lacinia. Habitant fermentum vitae ultrices vehicula dolor. Etiam malesuada praesent et libero cubilia. Est lacinia nunc conubia nullam proin lobortis quis eros. Ultrices efficitur ridiculus suscipit himenaeos platea. Et litora potenti inceptos phasellus auctor sed risus nec interdum. Luctus sollicitudin ex sagittis egestas faucibus lectus hac diam. Leo praesent aptent nulla pretium, torquent rutrum posuere per."
    )

    val i55 = listOf(
        "Lorem ipsum odor amet, consectetuer adipiscing elit. Faucibus habitasse conubia quis primis metus. Sit facilisi blandit elit convallis et aenean. Tincidunt vestibulum sem tempor facilisi lacinia. Auctor aptent nascetur nibh consectetur neque condimentum. Praesent ultricies porta nec curabitur vivamus posuere vel cubilia nisi? Faucibus accumsan proin ligula; fames quisque consectetur inceptos aliquet. Luctus molestie nec turpis ligula fermentum porttitor cubilia. In pellentesque tortor auctor eu mollis varius purus ligula.",
        "Amet rhoncus posuere posuere volutpat ultrices. Mattis euismod augue efficitur ut tellus ante. Pharetra molestie blandit ultricies blandit semper faucibus taciti nec. Nisl condimentum purus sed amet laoreet himenaeos. Pellentesque volutpat suspendisse quis ut dis. Mi habitasse eu elit hendrerit litora pellentesque tincidunt turpis. Purus orci himenaeos erat duis lorem et sodales penatibus velit.",
        "Donec vitae odio; adipiscing inceptos orci dis torquent. Parturient commodo vivamus arcu finibus libero montes hac ac. Pretium fusce placerat nunc primis placerat dis pretium. Augue vestibulum felis venenatis vehicula; proin at a? Erat ornare etiam primis euismod tempus lobortis. Ipsum molestie et nibh justo ipsum dolor ac cubilia. Facilisis litora semper leo fames blandit blandit. Porttitor conubia nam dis placerat purus faucibus laoreet. Duis bibendum feugiat netus parturient mus, congue potenti himenaeos.",
        "Efficitur vivamus tincidunt proin pulvinar elit vestibulum tristique fermentum. Mattis velit class turpis euismod per senectus amet. Consectetur velit proin vel at in, aliquet commodo arcu. Tincidunt parturient porta ipsum ullamcorper felis aliquam non. Sapien dapibus eleifend fames mi iaculis rhoncus nisi. Arcu dolor massa molestie euismod orci maecenas consequat senectus ornare.",
        "Semper maximus aenean blandit sed scelerisque mollis velit fusce lacinia. Habitant fermentum vitae ultrices vehicula dolor. Etiam malesuada praesent et libero cubilia. Est lacinia nunc conubia nullam proin lobortis quis eros. Ultrices efficitur ridiculus suscipit himenaeos platea. Et litora potenti inceptos phasellus auctor sed risus nec interdum. Luctus sollicitudin ex sagittis egestas faucibus lectus hac diam. Leo praesent aptent nulla pretium, torquent rutrum posuere per."
    )

    a1.forEach { chapterId ->
        for (image in i11.indices) {
            val order = image
            db?.execSQL("""
            INSERT INTO ${Contract.ParagraphEntry.TABLE_NAME} 
            (${Contract.ParagraphEntry.COLUMN_CONTENT_FILE}, ${Contract.ParagraphEntry.COLUMN_NUMBER_ORDER}, 
             ${Contract.ParagraphEntry.COLUMN_CHAPTER_ID_FK})
            VALUES ('${i11[image]}', $order, $chapterId)
        """)
        }
    }

    b1.forEach { chapterId ->
        for (image in i22.indices) {
            val order = image
            db?.execSQL("""
            INSERT INTO ${Contract.ImageEntry.TABLE_NAME} 
            (${Contract.ImageEntry.COLUMN_CONTENT_FILE}, ${Contract.ImageEntry.COLUMN_NUMBER_ORDER}, 
             ${Contract.ImageEntry.COLUMN_CHAPTER_ID_FK})
            VALUES ('${i22[image]}', $order, $chapterId)
        """)
        }
    }

    c1.forEach { chapterId ->
        for (image in i33.indices) {
            val order = image
            db?.execSQL("""
            INSERT INTO ${Contract.ImageEntry.TABLE_NAME} 
            (${Contract.ImageEntry.COLUMN_CONTENT_FILE}, ${Contract.ImageEntry.COLUMN_NUMBER_ORDER}, 
             ${Contract.ImageEntry.COLUMN_CHAPTER_ID_FK})
            VALUES ('${i33[image]}', $order, $chapterId)
        """)
        }
    }

    d1.forEach { chapterId ->
        for (image in i44.indices) {
            val order = image
            db?.execSQL("""
            INSERT INTO ${Contract.ImageEntry.TABLE_NAME} 
            (${Contract.ImageEntry.COLUMN_CONTENT_FILE}, ${Contract.ImageEntry.COLUMN_NUMBER_ORDER}, 
             ${Contract.ImageEntry.COLUMN_CHAPTER_ID_FK})
            VALUES ('${i44[image]}', $order, $chapterId)
        """)
        }
    }

    e.forEach { chapterId ->
        for (image in i55.indices) {
            val order = image
            db?.execSQL("""
            INSERT INTO ${Contract.ImageEntry.TABLE_NAME} 
            (${Contract.ImageEntry.COLUMN_CONTENT_FILE}, ${Contract.ImageEntry.COLUMN_NUMBER_ORDER}, 
             ${Contract.ImageEntry.COLUMN_CHAPTER_ID_FK})
            VALUES ('${i55[image]}', $order, $chapterId)
        """)
        }
    }

    // Tạo dữ liệu cho bảng Comment
    for (storyId in 1..100) {
        val commentCount = random.nextInt(8) + 3 // 3 - 10 comments
        for (i in 1..commentCount) {
            val userId = random.nextInt(7) + 1 // user_id từ 1 đến 7
            val content = "Comment content for story $storyId by user $userId"
            val time = "2024-12-${random.nextInt(30) + 1} ${random.nextInt(24)}:${random.nextInt(60)}:${random.nextInt(60)}"

            db?.execSQL(
                """
                INSERT INTO ${Contract.CommentEntry.TABLE_NAME} 
                (${Contract.CommentEntry.COLUMN_CONTENT}, ${Contract.CommentEntry.COLUMN_TIME}, 
                 ${Contract.CommentEntry.COLUMN_USER_ID_FK}, ${Contract.CommentEntry.COLUMN_STORY_ID_FK})
                VALUES (?, ?, ?, ?)
                """.trimIndent(), arrayOf(content, time, userId, storyId)
            )
        }
    }

    // Tạo dữ liệu cho bảng Rate
    for (storyId in 1..100) {
        val rateCount = random.nextInt(6) + 1 // 1 - 6 ratings
        val usedUserIds = mutableSetOf<Int>() // Đảm bảo không lặp user_id cho cùng story

        for (i in 1..rateCount) {
            var userId: Int
            do {
                userId = random.nextInt(7) + 1 // user_id từ 1 đến 7
            } while (usedUserIds.contains(userId))

            usedUserIds.add(userId)
            val rate = random.nextInt(5) + 1 // rate từ 1 đến 5

            db?.execSQL(
                """
                INSERT INTO ${Contract.RateEntry.TABLE_NAME} 
                (${Contract.RateEntry.COLUMN_RATE}, ${Contract.RateEntry.COLUMN_USER_ID_FK}, 
                 ${Contract.RateEntry.COLUMN_STORY_ID_FK})
                VALUES (?, ?, ?)
                """.trimIndent(), arrayOf(rate, userId, storyId)
            )
        }
    }
//    for (chapterId in 1..50) {
//
//
//        // Tạo 5 hình ảnh cho mỗi chapter
//        for (image in 1..listOfImageChapterUrl.size) {
//
//
//            val order = image
//
//            // Chèn dữ liệu hình ảnh vào bảng img_table
//            db?.execSQL("""
//                    INSERT INTO ${Contract.ImageEntry.TABLE_NAME}
//                    (${Contract.ImageEntry.COLUMN_CONTENT_FILE}, ${Contract.ImageEntry.COLUMN_NUMBER_ORDER},
//                     ${Contract.ImageEntry.COLUMN_CHAPTER_ID_FK})
//                    VALUES ('${listOfImageChapterUrl[image-1]}', $order, $chapterId)
//                """)
//        }
//    }

}




