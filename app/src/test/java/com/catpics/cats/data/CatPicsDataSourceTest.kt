package com.catpics.cats.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PageKeyedDataSource
import com.catpics.api.CatsApi
import com.catpics.model.Cat
import com.catpics.model.Category
import com.nhaarman.mockito_kotlin.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class CatPicsDataSourceTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private var mockInitialCallback: PageKeyedDataSource.LoadInitialCallback<Int, Cat> = mock()
    private var mockCallback: PageKeyedDataSource.LoadCallback<Int, Cat> = mock()
    private var mockService: CatsApi = mock()
    private var mockHttpException: HttpException = mock()
    private var mockResponse: Response<List<Cat>> = mock()
    private var cat: Cat = mock()

    private val mockInitialParams: PageKeyedDataSource.LoadInitialParams<Int> =
        PageKeyedDataSource.LoadInitialParams(2, true)
    private val mockParams: PageKeyedDataSource.LoadParams<Int> =
        PageKeyedDataSource.LoadParams(2, 12)

    private val catCaptor = argumentCaptor<List<Cat>>()
    private val previousPageKeyCaptor = argumentCaptor<Int>()
    private val nextPageKeyCaptor = argumentCaptor<Int>()

    private lateinit var catPicsDataSource: CatPicsDataSource

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        catPicsDataSource = CatPicsDataSource(testScope, mockService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `initial paging load failed`() = testDispatcher.runBlockingTest {

        whenever(mockService.getCats(any(), any(), any(), any())).thenThrow(mockHttpException)

        catPicsDataSource.loadInitial(mockInitialParams, mockInitialCallback)

        verify(mockInitialCallback, never()).onResult(emptyList(), null, 2)
    }

    @Test
    fun `initial paging load successful`() = testDispatcher.runBlockingTest {

        val cats: List<Cat> = listOf(cat, cat, cat)

        whenever(mockService.getCats(any(), any(), any(), any())).thenReturn(mockResponse)
        whenever(mockResponse.body()).thenReturn(cats)
        whenever(mockResponse.isSuccessful).thenReturn(true)
        whenever(cat.categories).thenReturn(emptyList())

        catPicsDataSource.loadInitial(mockInitialParams, mockInitialCallback)

        verify(mockInitialCallback).onResult(catCaptor.capture(), previousPageKeyCaptor.capture(), nextPageKeyCaptor.capture())
        assertEquals(3, catCaptor.firstValue.size)
        assertNull(previousPageKeyCaptor.firstValue)
        assertEquals(2, nextPageKeyCaptor.firstValue)
    }

    @Test
    fun `hat cats filtered out`() = testDispatcher.runBlockingTest {

        val category: Category = Category(id = 0, name = "normal")
        val hatCategory: Category = Category(id = 0, name = "hats")
        val cat: Cat = Cat(id = "1", url = "url", breeds = emptyList(), categories = listOf(category, category))
        val hatCat: Cat = Cat(id = "1", url = "url", breeds = emptyList(), categories = listOf(category, hatCategory))
        val cats: List<Cat> = listOf(cat, hatCat, hatCat, cat)

        whenever(mockService.getCats(any(), any(), any(), any())).thenReturn(mockResponse)
        whenever(mockResponse.body()).thenReturn(cats)
        whenever(mockResponse.isSuccessful).thenReturn(true)

        catPicsDataSource.loadInitial(mockInitialParams, mockInitialCallback)

        verify(mockInitialCallback).onResult(catCaptor.capture(), previousPageKeyCaptor.capture(), nextPageKeyCaptor.capture())
        assertEquals(2, catCaptor.firstValue.size)
        assertNull(previousPageKeyCaptor.firstValue)
        assertEquals(2, nextPageKeyCaptor.firstValue)
    }

    @Test
    fun `load after paging load failed`() = testDispatcher.runBlockingTest {

        whenever(mockService.getCats(any(), any(), any(), any())).thenThrow(mockHttpException)

        catPicsDataSource.loadAfter(mockParams, mockCallback)

        verify(mockCallback, never()).onResult(emptyList(), 3)
    }

    @Test
    fun `load after paging load successful`() = testDispatcher.runBlockingTest {

        whenever(mockService.getCats(any(), any(), any(), any())).thenReturn(mockResponse)
        whenever(mockResponse.isSuccessful).thenReturn(true)
        whenever(mockResponse.body()).thenReturn(emptyList())

        catPicsDataSource.loadAfter(mockParams, mockCallback)

        verify(mockCallback).onResult(emptyList(), 3)
    }
}
