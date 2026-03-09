import { useEffect, useState } from "react"
import { useParams, useNavigate } from "react-router-dom"
import { getActivityById, getActivityDetail } from "../services/api"
import { Box, Card, CardContent, Divider, Typography, CircularProgress, Button, Chip, Stack, Alert } from "@mui/material"
import ArrowBackIcon from "@mui/icons-material/ArrowBack"
import LocalFireDepartmentIcon from "@mui/icons-material/LocalFireDepartment"
import TimerIcon from "@mui/icons-material/Timer"
import DateRangeIcon from "@mui/icons-material/DateRange"
import TipsAndUpdatesIcon from "@mui/icons-material/TipsAndUpdates"
import CheckCircleIcon from "@mui/icons-material/CheckCircle"
import WarningIcon from "@mui/icons-material/Warning"

type Activity = {
  activityId: string
  userId: string
  activityType: string
  durationMinutes: number
  caloriesBurned: number
  startedAt: string
}

type Recommendation = {
  overallRecommendation: string
  improvementAreas?: string[]
  suggestions?: string[]
  safetyNotes?: string[]
}

const ActivityDetail = () => {
  const { id } = useParams()
  const navigate = useNavigate()

  const [activity, setActivity] = useState<Activity | null>(null)
  const [recommendation, setRecommendation] = useState<Recommendation | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchActivityDetail = async (activityId: string) => {
      setLoading(true)
      setError(null)
      try {
        const activityResponse = await getActivityById(activityId)
        setActivity(activityResponse.data)

        try {
          const recommendationResponse = await getActivityDetail(activityId)
          setRecommendation(recommendationResponse.data)
        } catch {
          setRecommendation(null)
        }

      } catch (error) {
        console.error("Failed to fetch activity detail:", error)
        setError("Failed to load activity details")
      } finally {
        setLoading(false)
      }
    }

    if (id) {
      fetchActivityDetail(id)
    }
  }, [id])

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", minHeight: "60vh" }}>
        <CircularProgress />
      </Box>
    )
  }

  if (error || !activity) {
    return (
      <Box sx={{ maxWidth: 800, mx: "auto" }}>
        <Button
          startIcon={<ArrowBackIcon />}
          onClick={() => navigate("/activities")}
          sx={{ mb: 2 }}
        >
          Back to Activities
        </Button>
        <Alert severity="error">
          {error || "Activity not found"}
        </Alert>
      </Box>
    )
  }

  return (
    <Box sx={{ maxWidth: 900, mx: "auto" }}>
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate("/activities")}
        sx={{ mb: 3, fontWeight: 600 }}
      >
        Back to Activities
      </Button>

      {/* Activity Details Card */}
      <Card sx={{ mb: 3, background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)", color: "white" }}>
        <CardContent sx={{ p: 3 }}>
          <Typography variant="h4" sx={{ fontWeight: 700, mb: 3 }}>
            {activity.activityType.replace(/_/g, " ")}
          </Typography>

          <Stack spacing={2}>
            <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
              <TimerIcon sx={{ fontSize: 24 }} />
              <Box>
                <Typography variant="body2" sx={{ opacity: 0.9 }}>Duration</Typography>
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  {activity.durationMinutes} minutes
                </Typography>
              </Box>
            </Box>

            <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
              <LocalFireDepartmentIcon sx={{ fontSize: 24, color: "#ff6b6b" }} />
              <Box>
                <Typography variant="body2" sx={{ opacity: 0.9 }}>Calories Burned</Typography>
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  {activity.caloriesBurned} calories
                </Typography>
              </Box>
            </Box>

            <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
              <DateRangeIcon sx={{ fontSize: 24 }} />
              <Box>
                <Typography variant="body2" sx={{ opacity: 0.9 }}>Date & Time</Typography>
                <Typography variant="body1" sx={{ fontWeight: 500 }}>
                  {new Date(activity.startedAt).toLocaleDateString()} at {new Date(activity.startedAt).toLocaleTimeString()}
                </Typography>
              </Box>
            </Box>
          </Stack>
        </CardContent>
      </Card>

      {/* Recommendation Card */}
      {recommendation && (
        <Card sx={{ mb: 3 }}>
          <CardContent sx={{ p: 3 }}>

            <Box sx={{ display: "flex", alignItems: "center", gap: 1, mb: 3 }}>
              <TipsAndUpdatesIcon sx={{ color: "primary.main", fontSize: 28 }} />
              <Typography variant="h5" sx={{ fontWeight: 700 }}>
                AI Recommendation
              </Typography>
            </Box>

            {/* Overall Analysis */}
            <Box sx={{ p: 2, backgroundColor: "#f5f7fa", borderRadius: 1, mb: 3 }}>
              <Typography variant="body1" sx={{ lineHeight: 1.8 }}>
                {recommendation.overallRecommendation}
              </Typography>
            </Box>

            {/* Improvements */}
            {recommendation.improvementAreas && recommendation.improvementAreas.length > 0 && (
              <>
                <Divider sx={{ my: 2 }} />

                <Box sx={{ display: "flex", alignItems: "center", gap: 1, mb: 2 }}>
                  <CheckCircleIcon sx={{ color: "success.main", fontSize: 24 }} />
                  <Typography variant="h6" sx={{ fontWeight: 600 }}>
                    Areas for Improvement
                  </Typography>
                </Box>

                <Stack spacing={1}>
                  {recommendation.improvementAreas.map((improvement, index) => (
                    <Chip
                      key={index}
                      label={improvement}
                      variant="outlined"
                      icon={<CheckCircleIcon />}
                      sx={{ justifyContent: "flex-start", height: "auto", py: 1 }}
                    />
                  ))}
                </Stack>
              </>
            )}

            {/* Suggestions */}
            {recommendation.suggestions && recommendation.suggestions.length > 0 && (
              <>
                <Divider sx={{ my: 2 }} />

                <Box sx={{ display: "flex", alignItems: "center", gap: 1, mb: 2 }}>
                  <TipsAndUpdatesIcon sx={{ color: "info.main", fontSize: 24 }} />
                  <Typography variant="h6" sx={{ fontWeight: 600 }}>
                    Suggestions
                  </Typography>
                </Box>

                <Stack spacing={1}>
                  {recommendation.suggestions.map((suggestion, index) => (
                    <Typography key={index} variant="body2" sx={{ pl: 3, borderLeft: "3px solid", borderColor: "info.main" }}>
                      {suggestion}
                    </Typography>
                  ))}
                </Stack>
              </>
            )}

            {/* Safety */}
            {recommendation.safetyNotes && recommendation.safetyNotes.length > 0 && (
              <>
                <Divider sx={{ my: 2 }} />

                <Box sx={{ display: "flex", alignItems: "center", gap: 1, mb: 2 }}>
                  <WarningIcon sx={{ color: "warning.main", fontSize: 24 }} />
                  <Typography variant="h6" sx={{ fontWeight: 600 }}>
                    Safety Guidelines
                  </Typography>
                </Box>

                <Stack spacing={1}>
                  {recommendation.safetyNotes.map((note, index) => (
                    <Alert key={index} severity="warning" sx={{ py: 1 }}>
                      {note}
                    </Alert>
                  ))}
                </Stack>
              </>
            )}

          </CardContent>
        </Card>
      )}

      {!recommendation && (
        <Card>
          <CardContent sx={{ textAlign: "center", py: 3 }}>
            <Typography color="text.secondary">
              No AI recommendation available for this activity yet.
            </Typography>
          </CardContent>
        </Card>
      )}
    </Box>
  )
}

export default ActivityDetail